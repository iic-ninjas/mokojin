package com.iic.mokojin.data;

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
public class CurrentSessionStore extends AbstractStore<MokojinBroadcastReceiver.SessionDataChangeBroadcastEvent, CurrentSessionStore.SessionUpdateEvent> {

    private static final String LOG_TAG = CurrentSessionStore.class.getName();
    public static class SessionUpdateEvent {}

    private List<QueueItem> mQueue = new ArrayList<>();
    private Match mCurrentMatch = null;

    public CurrentSessionStore(Bus broadcastEventBus) {
        super(broadcastEventBus, SessionUpdateEvent.class);
    }

    @Override
    protected Task<Void> onRefreshData() {
        return GetSessionData.getSessionData().onSuccess(new Continuation<Pair<Match, List<QueueItem>>, Void>() {
            @Override
            public Void then(Task<Pair<Match, List<QueueItem>>> pairTask) throws Exception {
                Pair<Match, List<QueueItem>> pair = pairTask.getResult();
                mCurrentMatch = pair.first;
                mQueue = pair.second;
                return null;
            }
        }, Task.UI_THREAD_EXECUTOR);
    }

    // These are here because Otto can't handle generic parameters, due to type erasure (Thanks, Java :( )
    @Subscribe
    @Override
    public void onBroadcastEvent(MokojinBroadcastReceiver.SessionDataChangeBroadcastEvent event) {
        super.onBroadcastEvent(event);
    }

    @Produce
    @Override
    public SessionUpdateEvent produceUpdateEvent() {
        return super.produceUpdateEvent();
    }

    public List<QueueItem> getQueue() {
        return mQueue;
    }

    public Match getCurrentMatch() {
        return mCurrentMatch;
    }
}