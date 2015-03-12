package com.iic.mokojin.modules;

import com.iic.mokojin.activities.CurrentMatchFragment;
import com.iic.mokojin.activities.PlayerQueueFragment;
import com.iic.mokojin.data.CurrentSessionStore;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yon on 3/12/15.
 */
@Module(
    injects = {
            CurrentMatchFragment.class,
            PlayerQueueFragment.class,
    }
)
public class DataModule {
    public static final String BROADCAST_RECEIVER_EVENT_BUS = "Broadcast Receiver";

    @Provides @Singleton Bus provideBroadcastEventBus() {
        return new Bus(BROADCAST_RECEIVER_EVENT_BUS);
    }

    @Provides @Singleton CurrentSessionStore provideCurrentSessionStore(Bus broadcastEventBus) {
        return new CurrentSessionStore(broadcastEventBus);
    }

}
