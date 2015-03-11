package com.iic.mokojin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.iic.mokojin.models.Person;
import com.iic.mokojin.models.Player;
import com.iic.mokojin.cloud.operations.CreatePersonOperation;
import com.iic.mokojin.cloud.operations.JoinQueueOperation;
import com.iic.mokojin.views.ProgressHudDialog;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import bolts.Continuation;
import bolts.Task;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnTextChanged;


public class AddPlayerActivity extends ActionBarActivity {

    private static final String LOG_TAG = AddPlayerActivity.class.getName();

    public static void launch(Context context){
        Intent addPlayerIntent = new Intent(context, AddPlayerActivity.class);
        context.startActivity(addPlayerIntent);
    }
    
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

        private PersonQueryAdapter mAdapter;
        private ProgressHudDialog mProgressDialog;

        public AddPlayerFragment() {
                    }

        @InjectView(R.id.people_list_view) ListView mPeopleListView;
        @InjectView(R.id.person_name_edittext) EditText mPersonNameEditText;
        @InjectView(R.id.create_person) ImageButton mAddPlayerButton;


        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            mAddPlayerButton.setEnabled(false);
        }

        // If there is a person selected in the listview - returns it (as a fulfilled promise)
        // Otherwise - creates a new person, and returns a promise which is fulfilled once that
        // person has been created
        private Task<Person> getPersonToJoin(@Nullable Person selectedPerson) {
            if (null == selectedPerson) {
                if (null != mProgressDialog){
                    mProgressDialog.setMessage(getResources().getString(R.string.create_user_progress));
                }
                return new CreatePersonOperation().run(getTextFieldValue());
            } else {
                return Task.forResult(selectedPerson);
            }
        }

        private String getTextFieldValue() {
            return mPersonNameEditText.getText().toString();
        }

        @SuppressWarnings("unused")
        @OnItemClick(R.id.people_list_view)
        void onListViewItemClick(int position) {
            selectPerson(mAdapter.getItem(position));
        }

        // param is null if nothing has been selected
        private void selectPerson(@Nullable Person selectedPerson) {
            mProgressDialog = new ProgressHudDialog(getActivity(), getResources().getString(R.string.join_queue_progress));
            mProgressDialog.show();
            getPersonToJoin(selectedPerson).continueWithTask(new Continuation<Person, Task<Player>>() {
                @Override
                public Task<Player> then(Task<Person> task) throws Exception {
                    if (task.isFaulted()) {
                        Log.e(LOG_TAG, "Error creating person", task.getError());
                        throw task.getError();
                    }
                    if (null != mProgressDialog) mProgressDialog.setMessage(getResources().getString(R.string.join_queue_progress));
                    return new JoinQueueOperation().run(task.getResult());
                }
            }, Task.UI_THREAD_EXECUTOR).continueWith(new Continuation<Player, Void>() {
                @Override
                public Void then(Task<Player> task) throws Exception {
                    if (null != mProgressDialog) mProgressDialog.hide();
                    mProgressDialog = null;
                    if (task.isFaulted()) {
                        Log.e(LOG_TAG, "Error joining queue", task.getError());
                        throw task.getError();
                    }
                    done(task.getResult());
                    return null;
                }
            }, Task.UI_THREAD_EXECUTOR).continueWith(new Continuation<Void, Object>() {
                @Override
                public Object then(Task<Void> task) throws Exception {
                    if (task.isFaulted()){
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Error Joining Queue")
                                .setMessage(task.getError().getMessage())
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // User clicked OK button
                                    }
                                })
                                .show();
                    }
                    return null;
                }
            }, Task.UI_THREAD_EXECUTOR);
        }

        private void done(Player player) {
            getActivity().finish();
        }
        
        
        @OnClick(R.id.create_person)
        void onClickCreatePerson(){
            selectPerson(null);
        }
        
        @OnTextChanged(R.id.person_name_edittext)
        void onPersonNameChanged(CharSequence text){
            mAddPlayerButton.setEnabled(text.length() > 0);
            mAdapter.getFilter().filter(text);
        }


        static class PersonQueryAdapter extends ParseQueryAdapter<Person> implements Filterable {

            private List<Person> mPeople;
            private List<Person> mFilteredPeople;

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

                addOnQueryLoadListener(new OnQueryLoadListener<Person>() {

                    @Override
                    public void onLoading() {

                    }

                    @Override
                    public void onLoaded(List<Person> people, Exception e) {
                        Collections.sort(people, new Comparator<Person>() {
                            @Override
                            public int compare(Person lhs, Person rhs) {
                                return lhs.getName().compareTo(rhs.getName());
                            }
                        });
                        mPeople = people;
                        getFilter().filter(null);
                    }
                });
            }

            @Override
            public Filter getFilter() {
                Filter filter = new Filter() {
                    @Override
                    protected FilterResults performFiltering(CharSequence constraint) {
                        FilterResults results = new FilterResults();
                        List<Person> filteredPeople = new ArrayList<>();

                        if (TextUtils.isEmpty(constraint)) {
                            filteredPeople = mPeople;
                        } else {
                            constraint = constraint.toString().toLowerCase();
                            for (int i = 0; i < mPeople.size(); i++) {
                                Person person = mPeople.get(i);
                                if (person.getName().toLowerCase().startsWith(constraint.toString()))  {
                                    filteredPeople.add(person);
                                }
                            }
                        }
                        results.count = filteredPeople.size();
                        results.values = filteredPeople;
                        return results;
                    }

                    @Override
                    protected void publishResults(CharSequence constraint, FilterResults results) {
                        mFilteredPeople = (List<Person>) results.values;
                        notifyDataSetChanged();
                    }
                };
                return filter;
            }

            static class PersonViewHolder {

                @InjectView(R.id.person_name)
                TextView personName;

                PersonViewHolder(View v) {
                    ButterKnife.inject(this, v);
                }
            }

            @Override
            public int getCount() {
                if (null == mFilteredPeople) return 0;
                return mFilteredPeople.size();
            }

            @Override
            public Person getItem(int index) {
                return mFilteredPeople.get(index);
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
