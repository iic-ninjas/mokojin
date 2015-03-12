package com.iic.mokojin.modules;

import com.iic.mokojin.activities.AddPlayerActivity;
import com.iic.mokojin.activities.ChooseCharactersActivity;
import com.iic.mokojin.activities.CurrentMatchFragment;
import com.iic.mokojin.activities.PlayerQueueFragment;
import com.iic.mokojin.data.CharacterStore;
import com.iic.mokojin.data.CurrentSessionStore;
import com.iic.mokojin.data.PeopleListStore;
import com.iic.mokojin.recievers.MokojinBroadcastReceiver;
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
            AddPlayerActivity.AddPlayerFragment.class,
            ChooseCharactersActivity.ChooseCharactersFragment.class,
            MokojinBroadcastReceiver.class
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

    @Provides @Singleton CharacterStore provideCharacterStore(Bus broadcastEventBus) {
        return new CharacterStore(broadcastEventBus);
    }

    @Provides @Singleton PeopleListStore providePeopleListStore(Bus broadcastEventBus) {
        return new PeopleListStore(broadcastEventBus);
    }

}
