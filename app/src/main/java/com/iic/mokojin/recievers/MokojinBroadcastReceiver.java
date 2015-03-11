package com.iic.mokojin.recievers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.squareup.otto.Bus;

/**
 * Created by giladgo on 3/11/15.
 */
public class MokojinBroadcastReceiver extends android.content.BroadcastReceiver {


    private static final String LOG_TAG = MokojinBroadcastReceiver.class.getName();

    private static Bus mEventBus = new Bus("BroadcastReceiver Event Bus");

    public static class SessionChangeBroadcastEvent { }
    public static class PeopleListChangeBroadcastEvent { }
    public static class CharacterListChangeChangeBroadcastEvent { }

    public MokojinBroadcastReceiver() {
        Log.v(LOG_TAG, "Creating broadcast receiver");
    }

    public static Bus getEventBus() {
        return mEventBus;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG, "received broadcast!");
        Log.d(LOG_TAG, intent.getStringExtra("com.parse.Data"));

        // TODO handle other types of events
        SessionChangeBroadcastEvent broadcastEvent = new SessionChangeBroadcastEvent();
        mEventBus.post(broadcastEvent);
    }

}
