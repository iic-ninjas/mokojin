package com.iic.mokojin.data;

import android.content.Context;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.iic.mokojin.Application;
import com.iic.mokojin.cloud.getters.GetPeople;
import com.iic.mokojin.models.Person;
import com.iic.mokojin.recievers.MokojinBroadcastReceiver;
import com.squareup.otto.Bus;
import com.squareup.otto.Produce;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import bolts.Continuation;
import bolts.Task;

/**
 * Created by giladgo on 3/11/15.
 */
public class PeopleListStore extends AbstractStore<MokojinBroadcastReceiver.PeopleListChangeBroadcastEvent, PeopleListStore.PeopleListUpdateEvent> {
    private static final String LOG_TAG = PeopleListStore.class.getName();

    private List<Person> mPeopleList = Collections.emptyList();

    public static class PeopleListUpdateEvent {
    }

    public static PeopleListStore get(Context context) {
        return ((Application)context.getApplicationContext()).getPeopleListStore();
    }

    public PeopleListStore(Bus broadcastEventBus) {
        super(broadcastEventBus, PeopleListUpdateEvent.class);
    }

    @Override
    protected Task<Void> onRefreshData() {
        return GetPeople.getPeople().onSuccess(new Continuation<List<Person>, Void>() {
            @Override
            public Void then(Task<List<Person>> task) throws Exception {
                mPeopleList = task.getResult();
                Collections.sort(mPeopleList, new Comparator<Person>() {
                    @Override
                    public int compare(Person lhs, Person rhs) {
                        return lhs.getName().compareTo(rhs.getName());
                    }
                });
                return null;
            }
        });
    }

    // These are here because Otto can't handle generic parameters, due to type erasure (Thanks, Java :( )
    @Subscribe
    @Override
    public void onBroadcastEvent(MokojinBroadcastReceiver.PeopleListChangeBroadcastEvent event) {
        super.onBroadcastEvent(event);
    }

    @Produce
    @Override
    public PeopleListUpdateEvent produceUpdateEvent() {
        return super.produceUpdateEvent();
    }

    public List<Person> getPeopleList() {
        return mPeopleList;
    }

    public List<Person> getPeopleListExculding(final List<Person> excludedPeople) {
        return new ArrayList<>(Collections2.filter(mPeopleList, new Predicate<Person>() {
            @Override
            public boolean apply(Person input) {
                return !excludedPeople.contains(input);
            }
        }));
    }


}
