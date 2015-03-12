package com.iic.mokojin.data;

import com.squareup.otto.Bus;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import bolts.Continuation;
import bolts.Task;

/**
 * Created by giladgo on 3/12/15.
 */
public abstract class AbstractStore<TBroadcastEvent, TStoreEvent> {

    private static final String LOG_TAG = AbstractStore.class.getName();

    private Bus mEventBus;
    private Constructor<? extends TStoreEvent> mStoreEventClassCtor;

    public Bus getEventBus() {
        return mEventBus;
    }

    public AbstractStore(Bus broadcastEventBus, Class<? extends TStoreEvent> storeEventClass) {
        mEventBus = new Bus("Current Session Store");

        mEventBus.register(this);
        broadcastEventBus.register(this);

        try {
            mStoreEventClassCtor = storeEventClass.getConstructor();
        } catch (NoSuchMethodException ignored) {

        }

        refreshData();
    }

    private void refreshData() {
        onRefreshData().onSuccess(new Continuation<Void, Void>() {
            @Override
            public Void then(Task<Void> task) throws Exception {
                getEventBus().post(produceUpdateEvent());
                return null;
            }
        }, Task.UI_THREAD_EXECUTOR);

    }

    protected abstract Task<Void> onRefreshData();

    // WHEN IMPLEMENTING THIS CLASS YOU MUST OVERRIDE THESE TWO METHODS, ANNOTATE THEM WITH
    // @Produce/@Subscribe appropriately, AND IN THE IMPLEMENTATION CALL SUPER, SEE CurrentSessionStore
    // FOR EXAMPLE
    protected void onBroadcastEvent(TBroadcastEvent event) {
        refreshData();
    }

    protected TStoreEvent produceUpdateEvent() {
        try {
            return mStoreEventClassCtor.newInstance();
        } catch (InstantiationException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
        } catch (InvocationTargetException e) {
            return null;
        }
    }


}
