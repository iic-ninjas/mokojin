package com.iic.mokojin;

import android.app.Application;

import com.iic.mokojin.models.Models;
import com.parse.Parse;

import org.robolectric.TestLifecycleApplication;

import java.lang.reflect.Method;

/**
 * Robolectric loads this Application instead of our real one :)
 * Created by yon on 3/5/15.
 */
public class TestApplication extends Application implements TestLifecycleApplication {

    @Override
    public void onCreate() {
        Models.registerModels();
        Parse.initialize(this, "aKkvKEvKOuRZDEDTTvOSIrj2pJBhuH0qJ8b8sZXW", "gTpsqUpkkDJ0ohV9k2mgbC11dN53Wp0kUGYvLGfH");
    }

    @Override
    public void beforeTest(Method method) {

    }

    @Override
    public void prepareTest(Object test) {

    }

    @Override
    public void afterTest(Method method) {

    }
}
