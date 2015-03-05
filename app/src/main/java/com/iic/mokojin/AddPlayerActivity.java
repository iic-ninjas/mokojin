package com.iic.mokojin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.collect.Lists;
import com.iic.mokojin.models.Person;
import com.iic.mokojin.models.Player;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;


public class AddPlayerActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_player);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new AddPlayerFragment())
                    .commit();
        }
    }



    public static class AddPlayerFragment extends Fragment {

        public AddPlayerFragment() {
            setHasOptionsMenu(true);
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            super.onCreateOptionsMenu(menu, inflater);
            // Inflate the menu; this adds items to the action bar if it is present.
            inflater.inflate(R.menu.menu_add_player, menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_done) {
                getActivity().finish();
                return true;
            }

            return super.onOptionsItemSelected(item);
        }

        @Override
        public void onResume() {
            ParseQueryAdapter<Person> adapter = new ParseQueryAdapter<>(getActivity(), new ParseQueryAdapter.QueryFactory<Person>() {
                    public ParseQuery<Person> create() {
                        ParseQuery<Player> hasMatch = new ParseQuery<>(Player.class);
                        ParseQuery<Player> hasQueueItem = new ParseQuery<>(Player.class);
                        // TODO do this

                        ParseQuery<Player> playerQuery = ParseQuery.or(Lists.<ParseQuery<Player>>newArrayList(hasMatch, hasQueueItem));

                        ParseQuery<Person> query = new ParseQuery<>(Person.class);
                        query.whereDoesNotMatchKeyInQuery("id", "person", playerQuery);
                        return query;
                    }
                });
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_add_player, container, false);
            return rootView;
        }
    }
}
