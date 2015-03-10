package com.iic.mokojin.utils;

import android.support.annotation.Nullable;

import com.iic.mokojin.models.Character;
import com.iic.mokojin.models.Match;
import com.iic.mokojin.models.Person;
import com.iic.mokojin.models.Player;
import com.parse.ParseObject;

import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by yon on 3/5/15.
 */
public final class MockFactory {

    public static Player createPlayer(@Nullable String objectID){
        return createObject(Player.class, objectID);
    }
    
    public static Character createCharacter(@Nullable String objectID){
        return createObject(Character.class, objectID);
    }
    
    public static Character createCharacterWithId(int characterId){
        Character mockChar = createCharacter(null);
        when(mockChar.getCharacterId()).thenReturn(characterId);
        return mockChar;
    }

    public static Person createPerson(@Nullable String objectId) {
        return createObject(Person.class, objectId);
    }
    
    public static Match createMatch(@Nullable String objectId){
        return createObject(Match.class, objectId);
    }

    private static String randomObjectId(){
        return UUID.randomUUID().toString();
    }
    
    private static <T extends ParseObject> T createObject(Class<T> klass, @Nullable String objectID){
        if (objectID == null) objectID = randomObjectId();
        T mockedObject = mock(klass);
        when(mockedObject.getObjectId()).thenReturn(objectID);
        return mockedObject;
    }
}
