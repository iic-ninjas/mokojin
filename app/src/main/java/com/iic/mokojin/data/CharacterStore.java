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
public class CharacterStore {
    private static final String LOG_TAG = CharacterStore.class.getName();

    private List<com.iic.mokojin.models.Character> mCharacters;
    private Bus mEventBus;

    public static class CharacterListUpdateEvent {
    }

    public Bus getEventBus() {
        return mEventBus;
    }

    public static CharacterStore get(Context context) {
        return ((Application)context.getApplicationContext()).getCharacterStore();
    }

    public CharacterStore(Bus broadcastEventBus) {
        mEventBus = new Bus("Character Store");
        mEventBus.register(this);
        broadcastEventBus.register(this);

        refreshData();
    }

    private void refreshData() {
        GetCharacters.getCharacters().onSuccess(new Continuation<List<Character>, Void>() {
            @Override
            public Void then(Task<List<Character>> task) throws Exception {
                mCharacters = task.getResult();
                mEventBus.post(produceCharacterUpdateEvent());
                return null;
            }
        });
    }

    @Subscribe
    public void onCharacterUpdate(MokojinBroadcastReceiver.CharacterListChangeChangeBroadcastEvent event) {
        refreshData();
    }

    @Produce
    public CharacterListUpdateEvent produceCharacterUpdateEvent() {
        return new CharacterListUpdateEvent();
    }


    public List<Character> getCharacters() {
        return mCharacters;
    }


}
