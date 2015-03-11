package com.iic.mokojin.data;

import android.content.Context;
import android.util.Log;

import com.iic.mokojin.Application;
import com.iic.mokojin.cloud.getters.GetPeople;
import com.iic.mokojin.models.Person;
import com.iic.mokojin.recievers.MokojinBroadcastReceiver;
import com.squareup.otto.Bus;
import com.squareup.otto.Produce;
import com.squareup.otto.Subscribe;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import bolts.Continuation;
import bolts.Task;

/**
 * Created by giladgo on 3/11/15.
 */
public class PeopleListStore {
    private static final String LOG_TAG = PeopleListStore.class.getName();

    private List<Person> mPeopleList;
    private Bus mEventBus;

    public static class PeopleListUpdateEvent {
    }

    public Bus getEventBus() {
        return mEventBus;
    }

    public static PeopleListStore get(Context context) {
        return ((Application)context.getApplicationContext()).getPeopleListStore();
    }

    public PeopleListStore(Bus broadcastEventBus) {
        mEventBus = new Bus("People List Store");
        mEventBus.register(this);
        broadcastEventBus.register(this);

        refreshData();
    }

    private void refreshData() {
        GetPeople.getPeople().onSuccess(new Continuation<List<Person>, Void>() {
            @Override
            public Void then(Task<List<Person>> task) throws Exception {
                mPeopleList = task.getResult();
                Collections.sort(mPeopleList, new Comparator<Person>() {
                    @Override
                    public int compare(Person lhs, Person rhs) {
                        return lhs.getName().compareTo(rhs.getName());
                    }
                });
                mEventBus.post(producePeopleListUpdateEvent());
                return null;
            }
        });
    }

    @Subscribe
    public void onPeopleListUpdate(MokojinBroadcastReceiver.PeopleListChangeBroadcastEvent event) {
        refreshData();
    }

    @Produce
    public PeopleListUpdateEvent producePeopleListUpdateEvent() {
        Log.v(LOG_TAG, "producing event");
        return new PeopleListUpdateEvent();
    }


    public List<Person> getPeopleList() {
        return mPeopleList;
    }



}
