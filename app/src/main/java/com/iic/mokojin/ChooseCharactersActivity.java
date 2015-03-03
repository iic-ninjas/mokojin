package com.iic.mokojin;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.iic.mokojin.models.Character;
import com.iic.mokojin.models.Player;
import com.iic.mokojin.operations.SetCharacetersOperation;
import com.iic.mokojin.presenters.CharacterPresenter;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import bolts.Continuation;
import bolts.Task;
import butterknife.ButterKnife;
import butterknife.InjectView;


public class ChooseCharactersActivity extends ActionBarActivity {
    
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
        private Integer mCharacter1idx;
        private Integer mCharacter2idx;

        public ChooseCharactersFragment() {
            setHasOptionsMenu(true);
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
            Task<Player> setCharacterTask = new SetCharacetersOperation().run(mPlayer, characterPair.first, characterPair.second);
            setCharacterTask.continueWith(new Continuation<Player, Object>() {
                @Override
                public Object then(Task<Player> task) throws Exception {
                    getActivity().finish();
                    return null;
                }
            }, Task.UI_THREAD_EXECUTOR);
        }

        private Pair<Character, Character> selectedCharacters() {
            if (mCharacter1idx != null && mCharacter2idx != null){
                return Pair.create(mCharacterAdapter.getItem(mCharacter1idx), mCharacterAdapter.getItem(mCharacter2idx));
            } else if (mCharacter1idx != null && mCharacter2idx == null){
                return Pair.create(mCharacterAdapter.getItem(mCharacter1idx), null);
            } else if (mCharacter1idx == null && mCharacter2idx != null){
                return Pair.create(mCharacterAdapter.getItem(mCharacter2idx), null);
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
            mCharacterListView.setAdapter(mCharacterAdapter);
            mCharacterListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (mCharacter1idx == null){
                        mCharacter1idx = position;
                    } else if (mCharacter1idx == position){
                        mCharacter1idx = null;
                        if (null != mCharacter2idx){
                            mCharacter1idx = mCharacter2idx;
                            mCharacter2idx = null;
                        }
                    } else if (mCharacter2idx == null) {
                        mCharacter2idx = position;
                    } else if (mCharacter2idx == position){
                        mCharacter2idx = null;
                    } else {
                        mCharacterListView.setItemChecked(mCharacter1idx, false);
                        mCharacter1idx = mCharacter2idx;
                        mCharacter2idx = position;
                    }
                    refreshDoneMenuItem();
                }
            });
            return rootView;
        }
        
        private void refreshDoneMenuItem(){
            mDoneMenuItem.setEnabled(validSelectionCount());
        }
        
        private boolean validSelectionCount(){
            Log.i("SELECTCHARS", String.format("%s %s", String.valueOf(mCharacter1idx), String.valueOf(mCharacter2idx)));
            return mCharacter1idx != null;
        }

    }
    
    private static class CharacterAdapter extends ParseQueryAdapter<Character>{

        public CharacterAdapter(Context context) {
            super(context, new QueryFactory<Character>() {
                @Override
                public ParseQuery<Character> create() {
                    ParseQuery<Character> query = ParseQuery.getQuery(Character.class);
                    query.orderByAscending("name");
                    return query;
                }
            });
            setObjectsPerPage(60);
        }

        // Customize the layout by overriding getItemView
        @Override
        public View getItemView(Character character, View v, ViewGroup parent) {
            if (v == null) {
                v = View.inflate(getContext(), R.layout.character_list_item, null);
            }
            super.getItemView(character, v, parent);

            TextView titleTextView = (TextView) v.findViewById(R.id.character_name);
            titleTextView.setText(character.getName());

            ImageView avatarView = (ImageView) v.findViewById(R.id.character_image);
            avatarView.setImageResource(CharacterPresenter.getImageResource(getContext(), character));

            return v;
        }
    }
}
