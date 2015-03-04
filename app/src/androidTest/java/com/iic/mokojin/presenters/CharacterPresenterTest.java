package com.iic.mokojin.presenters;

import android.test.AndroidTestCase;

import com.iic.mokojin.R;
import com.iic.mokojin.models.Character;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * Created by yon on 3/4/15.
 */
public class CharacterPresenterTest extends AndroidTestCase{

    private static final int TEST_CHARACTER_RESOURCE = R.drawable.player_10;

    public void testGetImageResourceForNoCharacter(){
        int resource = CharacterPresenter.getImageResource(getContext(), null);
        assertEquals(resource, CharacterPresenter.DEFAULT_IMAGE_RESOURCE);
    }

    public void testGetImageResource(){
        Character testCharacter = createCharacterWithId(10);
        int resource = CharacterPresenter.getImageResource(getContext(), testCharacter);
        assertEquals(resource, TEST_CHARACTER_RESOURCE);
    }

    private static Character createCharacterWithId(int characterId){
        Character mockChar = mock(Character.class);
        when(mockChar.getCharacterId()).thenReturn(characterId);
        return null;
    }
}
