package com.iic.mokojin.recievers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.iic.mokojin.Application;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by giladgo on 3/11/15.
 */
public class MokojinBroadcastReceiver extends android.content.BroadcastReceiver {

    private static final String LOG_TAG = MokojinBroadcastReceiver.class.getName();
    public static final String SESSION_DATA_CHANGED = "sessionDataChanged";

    public static class SessionDataChangeBroadcastEvent { }
    public static class PeopleListChangeBroadcastEvent { }
    public static class CharacterListChangeChangeBroadcastEvent { }

    public MokojinBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG, "received broadcast!");
        String dataString = intent.getStringExtra("com.parse.Data");

        try {
            JSONObject jsonObject = new JSONObject(dataString);
            String eventType = jsonObject.getString("type");

            Object event = null;

            if (eventType.equals(SESSION_DATA_CHANGED)) {
                event = new SessionDataChangeBroadcastEvent();
            }
            // TODO handle other types of events

            if (event != null) {
                ((Application)context.getApplicationContext()).getBroadcastReceiverEventBus().post(event);
            }
        }
        catch (JSONException ignored) {
            // If there was a parsing error, ignore the event
        }



    }

}
