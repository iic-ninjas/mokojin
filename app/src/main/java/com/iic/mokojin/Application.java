package com.iic.mokojin;

import com.iic.mokojin.models.Models;
import com.iic.mokojin.modules.AndroidModule;
import com.iic.mokojin.modules.DataModule;
import com.parse.Parse;

import java.util.Arrays;
import java.util.List;

import dagger.ObjectGraph;

/**
 * Created by udi on 3/3/15.
 */
public class Application extends android.app.Application {

    private ObjectGraph mObjectGraph;

    public void onCreate() {
        super.onCreate();
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Models.registerModels();

        Parse.initialize(this, "GeJyJhvvsIe540zKyn9rCZwSv7AIEcc11DHQjSAV", "40quo2Icf83unfXkDu2ZJjEcecPsHl03aqiuNsbH");

        mObjectGraph = ObjectGraph.create(getModules().toArray());
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
