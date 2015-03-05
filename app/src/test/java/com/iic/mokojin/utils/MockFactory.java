package com.iic.mokojin.utils;

import com.iic.mokojin.models.Character;
import com.iic.mokojin.models.Player;
import com.parse.ParseObject;

import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by yon on 3/5/15.
 */
public final class MockFactory {

    public static Player createPlayer(String objectID){
        return createObject(Player.class, objectID);
    }
    
    public static Character createCharacter(String objectID){
        return createObject(Character.class, objectID);
    }
    
    public static Character createCharacterWithId(int characterId){
        Character mockChar = createCharacter(null);
        when(mockChar.getCharacterId()).thenReturn(characterId);
        return mockChar;
    }

    private static String randomObjectId(){
        return UUID.randomUUID().toString();
    }
    
    private static <T extends ParseObject> T createObject(Class<T> klass, String objectID){
        if (objectID == null) objectID = randomObjectId();
        T mockedObject = mock(klass);
        when(mockedObject.getObjectId()).thenReturn(objectID);
        return mockedObject;
    }
}
