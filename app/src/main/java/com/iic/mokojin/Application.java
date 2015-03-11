package com.iic.mokojin;

import com.iic.mokojin.data.CurrentSessionStore;
import com.iic.mokojin.data.PeopleListStore;
import com.iic.mokojin.models.Models;
import com.parse.Parse;
import com.squareup.otto.Bus;

/**
 * Created by udi on 3/3/15.
 */
public class Application extends android.app.Application {

    private CurrentSessionStore mCurrentSessionStore;
    private PeopleListStore mPeopleListStore;

    private Bus mBroadcastReceiverEventBus;

    public void onCreate() {
        super.onCreate();
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Models.registerModels();

        Parse.initialize(this, "GeJyJhvvsIe540zKyn9rCZwSv7AIEcc11DHQjSAV", "40quo2Icf83unfXkDu2ZJjEcecPsHl03aqiuNsbH");

        mBroadcastReceiverEventBus = new Bus("Broadcast Receiver");
        mCurrentSessionStore = new CurrentSessionStore(mBroadcastReceiverEventBus);
        mPeopleListStore = new PeopleListStore(mBroadcastReceiverEventBus);
    }

    public CurrentSessionStore getCurrentSessionStore() {
        return mCurrentSessionStore;
    }

    public PeopleListStore getPeopleListStore() {
        return mPeopleListStore;
    }

    public Bus getBroadcastReceiverEventBus() {
        return mBroadcastReceiverEventBus;
    }
}
