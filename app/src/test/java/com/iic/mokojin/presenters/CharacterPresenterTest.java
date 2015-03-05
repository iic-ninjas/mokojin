package com.iic.mokojin.presenters;

import com.iic.mokojin.R;
import com.iic.mokojin.models.Character;
import com.iic.mokojin.utils.MockFactory;
import com.iic.mokojin.utils.RobolectricGradleTestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;

import static org.junit.Assert.assertEquals;

/**
 * Created by yon on 3/4/15.
 */
@RunWith(RobolectricGradleTestRunner.class)
public class CharacterPresenterTest {

    private static final int TEST_CHARACTER_RESOURCE = R.drawable.player_10;
    
    @Test
    public void testGetImageResourceForNoCharacter(){
        int resource = CharacterPresenter.getImageResource(Robolectric.application, null);
        assertEquals(resource, CharacterPresenter.DEFAULT_IMAGE_RESOURCE);
    }

    @Test
    public void testGetImageResource(){
        Character testCharacter = MockFactory.createCharacterWithId(10);
        int resource = CharacterPresenter.getImageResource(Robolectric.application, testCharacter);
        assertEquals(TEST_CHARACTER_RESOURCE, resource);
    }

}
