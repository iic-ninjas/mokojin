package com.iic.mokojin.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.iic.mokojin.R;
import com.iic.mokojin.cloud.operations.CreatePersonOperation;
import com.iic.mokojin.cloud.operations.JoinQueueOperation;
import com.iic.mokojin.data.PeopleListStore;
import com.iic.mokojin.models.Person;
import com.iic.mokojin.models.Player;
import com.iic.mokojin.views.ProgressHudDialog;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

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

    public static class AddPlayerFragment extends AbstractMokojinFragment {

        private PeopleAdapter mAdapter;
        private ProgressHudDialog mProgressDialog;
        @Inject PeopleListStore mPeopleListStore;

        @InjectView(R.id.people_list_view) ListView mPeopleListView;
        @InjectView(R.id.person_name_edittext) EditText mPersonNameEditText;
        @InjectView(R.id.create_person) ImageButton mAddPlayerButton;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_add_player, container, false);
            ButterKnife.inject(this, rootView);
            return rootView;
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            mAddPlayerButton.setEnabled(false);
        }

        @Override
        public void onStart() {
            super.onStart();
            mAdapter = new PeopleAdapter();
            mPeopleListView.setAdapter(mAdapter);

            mPeopleListStore.getEventBus().register(this);
        }

        @Override
        public void onStop() {
            super.onStop();
            mPeopleListStore.getEventBus().unregister(this);
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
                    if (null != mProgressDialog) mProgressDialog.dismiss();
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
                    if (task.isFaulted()) {
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

        @Subscribe
        public void refreshList(PeopleListStore.PeopleListUpdateEvent event) {
            mAdapter.getFilter().filter(null);
        }

        class PeopleAdapter extends BaseAdapter implements Filterable {

            private List<Person> mFilteredPeople;

            @Override
            public Filter getFilter() {
                return new Filter() {
                    @Override
                    protected FilterResults performFiltering(CharSequence constraint) {
                        FilterResults results = new FilterResults();
                        List<Person> filteredPeople = new ArrayList<>();

                        if (TextUtils.isEmpty(constraint)) {
                            filteredPeople = mPeopleListStore.getPeopleList();
                        } else {
                            constraint = constraint.toString().toLowerCase();
                            for (int i = 0; i < mPeopleListStore.getPeopleList().size(); i++) {
                                Person person = mPeopleListStore.getPeopleList().get(i);
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
            }

            class PersonViewHolder {

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
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = View.inflate(getActivity(), R.layout.person_list_item, null);
                    convertView.setTag(new PersonViewHolder(convertView));
                }
                PersonViewHolder viewHolder = (PersonViewHolder)convertView.getTag();
                Person person = getItem(position);

                viewHolder.personName.setText(person.getName());
                return convertView;
            }

        }
    }
}
