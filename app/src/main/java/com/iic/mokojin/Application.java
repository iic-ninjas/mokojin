package com.iic.mokojin;

import com.iic.mokojin.data.CurrentSession;
import com.iic.mokojin.models.Models;
import com.parse.Parse;

/**
 * Created by udi on 3/3/15.
 */
public class Application extends android.app.Application {
    public void onCreate() {
        super.onCreate();
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Models.registerModels();

        Parse.initialize(this, "GeJyJhvvsIe540zKyn9rCZwSv7AIEcc11DHQjSAV", "40quo2Icf83unfXkDu2ZJjEcecPsHl03aqiuNsbH");

        CurrentSession.getInstance();
    }
}
