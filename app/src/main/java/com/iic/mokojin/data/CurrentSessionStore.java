package com.iic.mokojin.data;

import android.content.Context;
import android.util.Pair;

import com.iic.mokojin.Application;
import com.iic.mokojin.cloud.getters.GetSessionData;
import com.iic.mokojin.models.Match;
import com.iic.mokojin.models.QueueItem;
import com.iic.mokojin.recievers.MokojinBroadcastReceiver;
import com.squareup.otto.Bus;
import com.squareup.otto.Produce;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import bolts.Continuation;
import bolts.Task;

/**
 * Created by giladgo on 3/10/15.
 */
public class CurrentSessionStore {

    private static final String LOG_TAG = CurrentSessionStore.class.getName();

    private Bus mEventBus;

    private List<QueueItem> mQueue = new ArrayList<>();
    private Match mCurrentMatch = null;

    public Bus getEventBus() {
        return mEventBus;
    }

    public static CurrentSessionStore get(Context context) {
        return ((Application)context.getApplicationContext()).getCurrentSessionStore();
    }


    public static class SessionUpdateEvent {
    }

    public CurrentSessionStore(Bus broadcastEventBus) {
        mEventBus = new Bus("Current Session Store");

        mEventBus.register(this);
        broadcastEventBus.register(this);

        refreshData();
    }

    public void refreshData() {
        GetSessionData.getSessionData().onSuccess(new Continuation<Pair<Match, List<QueueItem>>, Void>() {
            @Override
            public Void then(Task<Pair<Match, List<QueueItem>>> pairTask) throws Exception {
                Pair<Match, List<QueueItem>> pair = pairTask.getResult();
                mCurrentMatch = pair.first;
                mQueue = pair.second;

                mEventBus.post(produceSessionUpdateEvent());
                return null;
            }
        }, Task.UI_THREAD_EXECUTOR);
    }

    @Subscribe
    public void onSessionUpdate(MokojinBroadcastReceiver.SessionDataChangeBroadcastEvent event) {
        refreshData();
    }

    @Produce
    public SessionUpdateEvent produceSessionUpdateEvent() {
        return new SessionUpdateEvent();
    }

    public List<QueueItem> getQueue() {
        return mQueue;
    }

    public Match getCurrentMatch() {
        return mCurrentMatch;
    }


}
