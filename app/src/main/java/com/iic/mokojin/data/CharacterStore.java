package com.iic.mokojin.data;

import android.content.Context;

import com.iic.mokojin.Application;
import com.iic.mokojin.cloud.getters.GetCharacters;
import com.iic.mokojin.models.Character;
import com.iic.mokojin.recievers.MokojinBroadcastReceiver;
import com.squareup.otto.Bus;
import com.squareup.otto.Produce;
import com.squareup.otto.Subscribe;

import java.util.List;

import bolts.Continuation;
import bolts.Task;

/**
 * Created by giladgo on 3/11/15.
 */
public class CharacterStore extends AbstractStore<MokojinBroadcastReceiver.CharacterListChangeChangeBroadcastEvent, CharacterStore.CharacterListUpdateEvent> {
    private static final String LOG_TAG = CharacterStore.class.getName();

    private List<com.iic.mokojin.models.Character> mCharacters;

    public static class CharacterListUpdateEvent {
    }

    public static CharacterStore get(Context context) {
        return ((Application)context.getApplicationContext()).getCharacterStore();
    }

    public CharacterStore(Bus broadcastEventBus) {
        super(broadcastEventBus, CharacterListUpdateEvent.class);
    }

    @Override
    protected Task<Void> onRefreshData() {
        return GetCharacters.getCharacters().onSuccess(new Continuation<List<Character>, Void>() {
            @Override
            public Void then(Task<List<Character>> task) throws Exception {
                mCharacters = task.getResult();
                return null;
            }
        });
    }

    // These are here because Otto can't handle generic parameters, due to type erasure (Thanks, Java :( )
    @Subscribe
    @Override
    public void onBroadcastEvent(MokojinBroadcastReceiver.CharacterListChangeChangeBroadcastEvent event) {
        super.onBroadcastEvent(event);
    }

    @Produce
    @Override
    public CharacterListUpdateEvent produceUpdateEvent() {
        return super.produceUpdateEvent();
    }

    public List<Character> getCharacters() {
        return mCharacters;
    }


}
