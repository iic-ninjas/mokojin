package com.iic.mokojin.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.iic.mokojin.R;
import com.iic.mokojin.cloud.operations.SetCharactersOperation;
import com.iic.mokojin.data.CharacterStore;
import com.iic.mokojin.data.CurrentSessionStore;
import com.iic.mokojin.models.Character;
import com.iic.mokojin.models.Player;
import com.iic.mokojin.presenters.CharacterPresenter;
import com.iic.mokojin.views.ProgressHudDialog;
import com.squareup.otto.Subscribe;

import java.util.List;

import javax.inject.Inject;

import bolts.Continuation;
import bolts.Task;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;


public class ChooseCharactersActivity extends ActionBarActivity {

    public static final String PLAYER_EXTRA = "PLAYER_EXT";
    private String mTitle;

    public static void chooseCharacter(Activity context, Player player) {
        Intent selectCharacterIntent = new Intent(context, ChooseCharactersActivity.class);

        player.saveToLocalStorage();
        selectCharacterIntent.putExtra(PLAYER_EXTRA, player.getObjectId());
        ActivityCompat.startActivity(context,
                selectCharacterIntent,
                ActivityOptionsCompat.makeSceneTransitionAnimation(context).toBundle());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_character);
        mTitle = getTitle().toString();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ChooseCharactersFragment())
                    .commit();
        }
    }
    
    public void updateTitle(int count){
        setTitle(mTitle.concat(String.format(" (%d)", count)));
    }

    public static class ChooseCharactersFragment extends AbstractMokojinFragment {
        
        @InjectView(R.id.character_list_view) GridView mCharacterListView;
        private CharacterAdapter mCharacterAdapter;
        private MenuItem mDoneMenuItem;

        private Player mPlayer;
        private Integer mCharacterA;
        private Integer mCharacterB;
        @Inject CharacterStore mCharacterStore;
        @Inject CurrentSessionStore mCurrentSessionStore;

        public ChooseCharactersFragment() {
            setHasOptionsMenu(true);
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            Intent intent = getActivity().getIntent();
            if (null != intent){
                String playerId = intent.getStringExtra(PLAYER_EXTRA);
                mPlayer = Player.loadFromLocalStorage(playerId);
            }
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            super.onCreateOptionsMenu(menu, inflater);
            inflater.inflate(R.menu.menu_choose_character, menu);
            mDoneMenuItem = menu.findItem(R.id.action_done);
            refreshMenu();
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            if (item.getItemId() == R.id.action_done){
                performDone();
            }
            return super.onOptionsItemSelected(item);
        }

        private void performDone() {
            Pair<Character, Character> characterPair = selectedCharacters();
            Task<Player> setCharacterTask = new SetCharactersOperation().run(mPlayer, characterPair.first, characterPair.second);
            final Dialog progressDialog = new ProgressHudDialog(getActivity(), getActivity().getString(R.string.updating_characters_progress));
            progressDialog.show();
            setCharacterTask.onSuccess(new Continuation<Player, Void>() {
                @Override
                public Void then(Task<Player> task) throws Exception {
                    progressDialog.dismiss();
                    mCurrentSessionStore.refreshData();
                    ActivityCompat.finishAfterTransition(getActivity());
                    return null;
                }
            }, Task.UI_THREAD_EXECUTOR);
        }

        private Pair<Character, Character> selectedCharacters() {
            if (mCharacterA != null && mCharacterB != null){
                return Pair.create(mCharacterAdapter.getItem(mCharacterA), mCharacterAdapter.getItem(mCharacterB));
            } else if (mCharacterA != null && mCharacterB == null){
                return Pair.create(mCharacterAdapter.getItem(mCharacterA), null);
            } else if (mCharacterA == null && mCharacterB != null){
                return Pair.create(mCharacterAdapter.getItem(mCharacterB), null);
            } else {
                return null;
            }
        }

        @Override
        public void onStart() {
            super.onStart();
            mCharacterStore.getEventBus().register(this);
        }

        @Override
        public void onStop() {
            super.onStop();
            mCharacterStore.getEventBus().unregister(this);
        }

        @Subscribe
        public void refreshCharacters(CharacterStore.CharacterListUpdateEvent event) {
            List<Character> characters = mCharacterStore.getCharacters();

            if (null != mPlayer && mCharacterA == null && mCharacterB == null){
                if (null != mPlayer.getCharacterA()) {
                    mCharacterA = characters.indexOf(mPlayer.getCharacterA());
                    if (null != mCharacterA) {
                        mCharacterListView.setItemChecked(mCharacterA, true);
                        mCharacterListView.smoothScrollToPosition(mCharacterA);
                    }
                }
                if (null != mPlayer.getCharacterB()) {
                    mCharacterB = characters.indexOf(mPlayer.getCharacterB());
                    if (null != mCharacterB) mCharacterListView.setItemChecked(mCharacterB, true);
                }
            }

            mCharacterAdapter = new CharacterAdapter(getActivity(), characters);
            mCharacterListView.setAdapter(mCharacterAdapter);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.choose_character_fragment, container, false);
            ButterKnife.inject(this, rootView);


            return rootView;
        }

        @OnItemClick(R.id.character_list_view)
        void onCharacterClick(int position) {
            if (mCharacterA == null){
                mCharacterA = position;
            } else if (mCharacterA == position){
                mCharacterA = null;
                if (null != mCharacterB){
                    mCharacterA = mCharacterB;
                    mCharacterB = null;
                }
            } else if (mCharacterB == null) {
                mCharacterB = position;
            } else if (mCharacterB == position){
                mCharacterB = null;
            } else {
                mCharacterListView.setItemChecked(mCharacterA, false);
                mCharacterA = mCharacterB;
                mCharacterB = position;
            }
            refreshMenu();
        }

        private void refreshMenu(){
            mDoneMenuItem.setEnabled(validSelectionCount());
            updateTitle();
        }

        private void updateTitle() {
            ChooseCharactersActivity charactersActivity = (ChooseCharactersActivity) getActivity();
            if (null == charactersActivity) return;
            if (null != mCharacterA){
                if (null != mCharacterB) {
                    charactersActivity.updateTitle(2);
                } else {
                    charactersActivity.updateTitle(1);
                }
            } else {
                charactersActivity.updateTitle(0);
            }
        }

        private boolean validSelectionCount(){
            return mCharacterA != null;
        }

    }

    static class CharacterAdapter extends BaseAdapter {

        private Context mContext;
        private List<Character> mCharacters;

        public CharacterAdapter(Context context, List<Character> characters) {
            mCharacters = characters;
            mContext = context;
        }


        @Override
        public int getCount() {
            return mCharacters.size();
        }

        @Override
        public Character getItem(int position) {
            return mCharacters.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.character_list_item, null);
                convertView.setTag(new CharacterViewHolder(convertView));
            }

            Character character = getItem(position);
            CharacterViewHolder viewHolder = (CharacterViewHolder) convertView.getTag();
            viewHolder.textView.setText(character.getName());
            viewHolder.imageView.setImageResource(CharacterPresenter.getImageResource(mContext, character));

            return convertView;
        }

        class CharacterViewHolder {
            @InjectView(R.id.character_name) TextView textView;
            @InjectView(R.id.character_image) ImageView imageView;
            
            public CharacterViewHolder(View view){
                ButterKnife.inject(this, view);
            }
            
        }
    }
}
