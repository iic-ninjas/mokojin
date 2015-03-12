package com.iic.mokojin;

import android.util.Log;

import com.iic.mokojin.models.Models;
import com.iic.mokojin.modules.AndroidModule;
import com.iic.mokojin.modules.DataModule;
import com.parse.Parse;
import com.parse.ParsePush;

import java.util.Arrays;
import java.util.List;

import bolts.Continuation;
import bolts.Task;
import dagger.ObjectGraph;

/**
 * Created by udi on 3/3/15.
 */
public class Application extends android.app.Application {

    private static final String LOG_TAG = Application.class.getSimpleName();
    private ObjectGraph mObjectGraph;

    public void onCreate() {
        super.onCreate();
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Models.registerModels();

        Parse.initialize(this, "GeJyJhvvsIe540zKyn9rCZwSv7AIEcc11DHQjSAV", "40quo2Icf83unfXkDu2ZJjEcecPsHl03aqiuNsbH");

        mObjectGraph = ObjectGraph.create(getModules().toArray());

        ParsePush.subscribeInBackground("").continueWith(new Continuation<Void, Void>() {
            @Override
            public Void then(Task<Void> task) throws Exception {
                if (task.isFaulted()) {
                    Log.e(LOG_TAG, "Error registering to push notifications", task.getError());
                } else {
                    Log.v(LOG_TAG, "Registerred successfully to push notifications");
                }
                return null;
            }
        });
    }

    protected List<Object> getModules() {
        return Arrays.asList(
                new AndroidModule(Application.this),
                new DataModule()
        );
    }

    public <T> T inject(T object) {
        return mObjectGraph.inject(object);
    }
}
