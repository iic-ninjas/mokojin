package com.iic.mokojin;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.iic.mokojin.models.Person;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;


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

        private ParseQueryAdapter<Person> mAdapter;

        public AddPlayerFragment() {
            setHasOptionsMenu(true);
        }

        @InjectView(R.id.people_list_view) ListView mPeopleListView;
        @InjectView(R.id.person_name_edittext) EditText mPersonNAmeEditText;

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
                done();
                return true;
            }

            return super.onOptionsItemSelected(item);
        }

        private void done() {
            Person personToJoin = null;
            if (!TextUtils.isEmpty(mPersonNAmeEditText.getText())) {
                // Create new person and then join
            } else {
                personToJoin = mAdapter.getItem(mPeopleListView.getSelectedItemPosition());
            }

            if (personToJoin != null) {

            }
        }



        static class PersonQueryAdapter extends ParseQueryAdapter<Person> {

            private static void filterPeopleCurrentlyPlaying(ParseQuery<Person> query) {

                // TODO

//            ParseQuery<Player> hasMatch = new ParseQuery<>(Player.class);
//            ParseQuery<Player> hasQueueItem = new ParseQuery<>(Player.class);
//
//
//            List<ParseQuery<Player>> conditions = Lists.newArrayListWithCapacity(2);
//            conditions.add(hasMatch);
//            conditions.add(hasQueueItem);
//            ParseQuery<Player> playerQuery = ParseQuery.or(conditions);
//
//
//            query.whereDoesNotMatchKeyInQuery("id", "person", playerQuery);
            }


            public PersonQueryAdapter(Context context) {
                super(context, new ParseQueryAdapter.QueryFactory<Person>() {
                    public ParseQuery<Person> create() {
                        ParseQuery<Person> query = new ParseQuery<>(Person.class);
                        filterPeopleCurrentlyPlaying(query);
                        return query;
                    }
                });
            }

            static class PersonViewHolder {

                @InjectView(R.id.person_name)
                TextView personName;

                PersonViewHolder(View v) {
                    ButterKnife.inject(this, v);
                }
            }

            @Override
            public View getItemView(com.iic.mokojin.models.Person person, View v, ViewGroup parent) {
                if (v == null) {
                    v = View.inflate(getContext(), R.layout.person_list_item, null);
                    v.setTag(new PersonViewHolder(v));
                }
                super.getItemView(person, v, parent);
                PersonViewHolder viewHolder = (PersonViewHolder)v.getTag();

                viewHolder.personName.setText(person.getName());
                return v;
            }
        }

        @Override
        public void onStart() {
            super.onStart();
            mAdapter = new PersonQueryAdapter(getActivity());

            mAdapter.setTextKey("name");

            mPeopleListView.setAdapter(mAdapter);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_add_player, container, false);
            ButterKnife.inject(this, rootView);
            return rootView;
        }
    }
}
