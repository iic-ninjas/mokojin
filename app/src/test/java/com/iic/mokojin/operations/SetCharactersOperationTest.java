package com.iic.mokojin.operations;

import com.iic.mokojin.models.Player;
import com.iic.mokojin.utils.ModelFactory;
import com.parse.ParseCloud;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import bolts.Task;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.anyMap;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by yon on 3/4/15.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ParseCloud.class})
public class SetCharactersOperationTest  {


    public static final String PLAYER_ID = "PLAYER_ID";
    public static final Player PLAYER = ModelFactory.createPlayer(PLAYER_ID);
    public static final String CHAR_A_ID = "CHAR_ID_1";
    public static final com.iic.mokojin.models.Character CHARACTER_A = ModelFactory.createCharacter(CHAR_A_ID);
    public static final String CHAR_B_ID = "CHAR_ID_2";
    public static final com.iic.mokojin.models.Character CHARACTER_B = ModelFactory.createCharacter(CHAR_B_ID);
    
    private static final Task MOCK_TASK = mock(Task.class);
    
    @Before
    public void mockParseCloud(){
        PowerMockito.mockStatic(ParseCloud.class);
        when(ParseCloud.callFunctionInBackground(anyString(), anyMap())).thenReturn(MOCK_TASK);
    }

    @Test
    public void shouldCallParseCloudWithCorrectAttributes(){
        SetCharactersOperation operation = new SetCharactersOperation();
        Task<Player> task = operation.run(PLAYER, CHARACTER_A, CHARACTER_B);
        assertEquals(MOCK_TASK, task);
        
        PowerMockito.verifyStatic();
        operation.run(PLAYER, CHARACTER_A, CHARACTER_B);
    }

    @Test
    public void shouldWorkWhenSendingOnlyOneCharacter(){
        SetCharactersOperation operation = new SetCharactersOperation();
        Task<Player> task = operation.run(PLAYER, CHARACTER_A, null);
        assertEquals(MOCK_TASK, task);

        PowerMockito.verifyStatic();
        operation.run(PLAYER, CHARACTER_A, null);
    }




}
