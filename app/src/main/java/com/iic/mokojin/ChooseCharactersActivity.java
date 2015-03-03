package com.iic.mokojin;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.iic.mokojin.models.Character;
import com.iic.mokojin.models.Player;
import com.iic.mokojin.operations.SetCharacetersOperation;
import com.iic.mokojin.presenters.CharacterPresenter;
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class ChooseCharactersFragment extends Fragment {

        @InjectView(R.id.character_list_view) ListView mCharacterListView;
        private CharacterAdapter mCharacterAdapter;
        private MenuItem mDoneMenuItem;
        private Player mPlayer;

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
            Character[] characters = selectedCharacters();
            Task<Player> setCharacterTask = new SetCharacetersOperation().run(mPlayer, characters[0], characters[1]);
            setCharacterTask.continueWith(new Continuation<Player, Object>() {
                @Override
                public Object then(Task<Player> task) throws Exception {
                    getActivity().finish();
                    return null;
                }
            }, Task.UI_THREAD_EXECUTOR);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_choose_character, container, false);
            ButterKnife.inject(this, rootView);

            mCharacterAdapter = new CharacterAdapter(getActivity());
            mCharacterListView.setAdapter(mCharacterAdapter);
            mCharacterListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    refreshDoneMenuItem();
                }
            });
            return rootView;
        }
        
        private void refreshDoneMenuItem(){
            mDoneMenuItem.setEnabled(validSelectionCount());
        }
        
        private boolean validSelectionCount(){
            SparseBooleanArray positions = mCharacterListView.getCheckedItemPositions();
            int counter = 0;
            for (int i = 0; i < mCharacterAdapter.getCount(); i++){
                if (positions.get(i)) counter++;
            }
            return counter == 1 || counter == 2;
        }

        private Character[] selectedCharacters(){
            SparseBooleanArray positions = mCharacterListView.getCheckedItemPositions();
            Character[] selectedChars = new Character[2];
            int idx = 0;
            for (int i = 0; i < mCharacterAdapter.getCount(); i++){
                if (positions.get(i)){
                    selectedChars[idx++] = mCharacterAdapter.getItem(i);
                }
            }
            return selectedChars;
        }
    }
    
    private static class CharacterAdapter extends ParseQueryAdapter<Character>{
        
        public CharacterAdapter(Context context) {
            super(context, Character.class);
            setObjectsPerPage(60);
        }
        
        // Customize the layout by overriding getItemView
        @Override
        public View getItemView(Character character, View v, ViewGroup parent) {
            if (v == null) {
                v = View.inflate(getContext(), R.layout.character_list_item, null);
            }
            super.getItemView(character, v, parent);

            // Add the title view
            TextView titleTextView = (TextView) v.findViewById(R.id.character_name);
            titleTextView.setText(character.getName());

            ImageView avatarView = (ImageView) v.findViewById(R.id.character_image);
            avatarView.setImageResource(CharacterPresenter.getImageResource(getContext(), character));

            return v;
        }
    }
}
