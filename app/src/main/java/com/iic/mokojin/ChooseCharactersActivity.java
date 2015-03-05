package com.iic.mokojin;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.iic.mokojin.models.Character;
import com.iic.mokojin.models.Player;
import com.iic.mokojin.operations.SetCharactersOperation;
import com.iic.mokojin.presenters.CharacterPresenter;
import com.iic.mokojin.views.ProgressHudDialog;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.util.List;

import bolts.Continuation;
import bolts.Task;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;


public class ChooseCharactersActivity extends ActionBarActivity {

    public static final String PLAYER_EXTRA = "PLAYER_EXT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_character);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ChooseCharactersFragment())
                    .commit();
        }
        
    }

    public static class ChooseCharactersFragment extends Fragment {
        
        @InjectView(R.id.character_list_view) GridView mCharacterListView;
        private CharacterAdapter mCharacterAdapter;
        private MenuItem mDoneMenuItem;

        private Player mPlayer;
        private Integer mCharacterA;
        private Integer mCharacterB;

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
            refreshDoneMenuItem();
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
            setCharacterTask.onSuccess(new Continuation<Player, Object>() {
                @Override
                public Object then(Task<Player> task) throws Exception {
                    Player player = task.getResult();
                    progressDialog.dismiss();
                    //TODO: return the updated player.
                    getActivity().finish();
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
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.choose_character_fragment, container, false);
            ButterKnife.inject(this, rootView);

            mCharacterAdapter = new CharacterAdapter(getActivity());
            mCharacterAdapter.addOnQueryLoadListener(new ParseQueryAdapter.OnQueryLoadListener<Character>() {
                @Override
                public void onLoading() {
                }

                @Override
                public void onLoaded(List<Character> characters, Exception e) {
                    if (null != mPlayer && mCharacterA == null && mCharacterB == null){
                        if (null != mPlayer.getCharacterA()) {
                            mCharacterA = characters.indexOf(mPlayer.getCharacterA());
                            if (null != mCharacterA) mCharacterListView.setItemChecked(mCharacterA, true);
                        }
                        if (null != mPlayer.getCharacterB()) {
                            mCharacterB = characters.indexOf(mPlayer.getCharacterB());
                            if (null != mCharacterB) mCharacterListView.setItemChecked(mCharacterB, true);
                        }
                    }
                }
            });
            mCharacterListView.setAdapter(mCharacterAdapter);
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
            refreshDoneMenuItem();
        }

        private void refreshDoneMenuItem(){
            mDoneMenuItem.setEnabled(validSelectionCount());
        }
        
        private boolean validSelectionCount(){
            return mCharacterA != null;
        }

    }
    
    static class CharacterAdapter extends ParseQueryAdapter<Character>{

        public CharacterAdapter(Context context) {
            super(context, new QueryFactory<Character>() {
                @Override
                public ParseQuery<Character> create() {
                    ParseQuery<Character> query = ParseQuery.getQuery(Character.class);
                    query.orderByAscending("name");
                    return query;
                }
            });
            setPaginationEnabled(false);
        }

        // Customize the layout by overriding getItemView
        @Override
        public View getItemView(Character character, View v, ViewGroup parent) {
            if (v == null) {
                v = View.inflate(getContext(), R.layout.character_list_item, null);
                v.setTag(new CharacterViewHolder(v));
            }
            super.getItemView(character, v, parent);

            CharacterViewHolder viewHolder = (CharacterViewHolder) v.getTag();
            viewHolder.textView.setText(character.getName());
            viewHolder.imageView.setImageResource(CharacterPresenter.getImageResource(getContext(), character));

            return v;
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
