package com.iic.mokojin;

import android.app.Application;

import com.iic.mokojin.models.Models;
import com.parse.Parse;

/**
 * Created by udi on 3/3/15.
 */
public class ApplicationController extends Application {
    public void onCreate() {
        super.onCreate();
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Models.registerModels();

        Parse.initialize(this, "GeJyJhvvsIe540zKyn9rCZwSv7AIEcc11DHQjSAV", "40quo2Icf83unfXkDu2ZJjEcecPsHl03aqiuNsbH");
    }
}
