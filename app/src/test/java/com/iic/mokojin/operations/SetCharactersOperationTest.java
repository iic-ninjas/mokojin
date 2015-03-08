package com.iic.mokojin.operations;

import com.iic.mokojin.models.Player;
import com.iic.mokojin.utils.MockFactory;

import org.junit.Test;

import java.util.HashMap;

/**
 * Created by yon on 3/4/15.
 */

public class SetCharactersOperationTest extends AbstractOperationTest {

    public static final String PLAYER_ID = "PLAYER_ID";
    public static final Player PLAYER = MockFactory.createPlayer(PLAYER_ID);
    public static final String CHAR_A_ID = "CHAR_ID_1";
    public static final com.iic.mokojin.models.Character CHARACTER_A = MockFactory.createCharacter(CHAR_A_ID);
    public static final String CHAR_B_ID = "CHAR_ID_2";
    public static final com.iic.mokojin.models.Character CHARACTER_B = MockFactory.createCharacter(CHAR_B_ID);

    @Test
    public void shouldCallParseCloudWithCorrectAttributes(){
        SetCharactersOperation operation = new SetCharactersOperation();

        Object retVal = operation.run(PLAYER, CHARACTER_A, CHARACTER_B);
        assertOperationReturnValue(retVal);

        HashMap<String, Object> expectedParams = new HashMap<>();
        expectedParams.put("player",  PLAYER_ID);
        expectedParams.put("characterA", CHAR_A_ID);
        expectedParams.put("characterB", CHAR_B_ID);

        assertOperationParams(SetCharactersOperation.SET_CHARACTER_CLOUD_FUNCTION_NAME, expectedParams);

    }

}
