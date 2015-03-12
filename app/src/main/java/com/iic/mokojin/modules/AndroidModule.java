package com.iic.mokojin.modules;

import android.content.Context;

import com.iic.mokojin.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yon on 3/12/15.
 */
@Module(
    library = true
)
public class AndroidModule {

    private final Application mApplication;

    public AndroidModule(Application application){
        this.mApplication = application;
    }

    @Provides @Singleton public Context provideApplicationContext() {
        return mApplication;
    }

}
