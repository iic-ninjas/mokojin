package com.iic.mokojin.data;

import android.util.Log;
import android.util.Pair;

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
public class CurrentSession {

    private static final String LOG_TAG = CurrentSession.class.getName();

    private static CurrentSession mCurrentSession;

    private List<QueueItem> mQueue = new ArrayList<>();
    private Match mCurrentMatch = null;

    // TODO: DI
    private Bus mEventBus = DataEventBus.getEventBus();

    public static class SessionUpdateEvent {
    }

    public static CurrentSession getInstance() {
        if (mCurrentSession == null) {
            mCurrentSession = new CurrentSession();
        }
        return mCurrentSession;
    }

    private CurrentSession() {
        mEventBus.register(this);
        MokojinBroadcastReceiver.getEventBus().register(this);
        refreshData();
    }

    private void refreshData() {
        GetSessionData.getSessionData().onSuccess(new Continuation<Pair<Match, List<QueueItem>>, Void>() {
            @Override
            public Void then(Task<Pair<Match, List<QueueItem>>> pairTask) throws Exception {
                Pair<Match, List<QueueItem>> pair = pairTask.getResult();
                Log.v(LOG_TAG, "Setting current match");
                mCurrentMatch = pair.first;
                mQueue = pair.second;

                mEventBus.post(produceSessionUpdateEvent());
                return null;
            }
        }, Task.UI_THREAD_EXECUTOR);
    }

    @Subscribe
    public void onSessionUpdate(MokojinBroadcastReceiver.SessionChangeBroadcastEvent event) {
        Log.v(LOG_TAG, "Got session change broadcast event");
        refreshData();
    }

    @Produce
    public SessionUpdateEvent produceSessionUpdateEvent() {
        Log.v(LOG_TAG, "producing event");
        return new SessionUpdateEvent();
    }

    public List<QueueItem> getQueue() {
        return mQueue;
    }

    public Match getCurrentMatch() {
        Log.v(LOG_TAG, "getting current match, is it null? " + Boolean.toString(mCurrentMatch == null));
        return mCurrentMatch;
    }


}
