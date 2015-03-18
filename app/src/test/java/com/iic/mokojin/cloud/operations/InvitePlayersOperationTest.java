package com.iic.mokojin.cloud.operations;

import com.iic.mokojin.utils.MockFactory;
import com.parse.ParseInstallation;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;

import static org.mockito.Mockito.when;

/**
 * Created by udi on 3/15/15.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ParseInstallation.class})
public class InvitePlayersOperationTest extends AbstractOperationTest {

    public static final String INSTALLATION_ID = "INSTALLATION_ID";
    public static final ParseInstallation INSTALLATION = MockFactory.createParseInstallation(INSTALLATION_ID);

    @Before
    public void mockParseInstallation(){
        PowerMockito.mockStatic(ParseInstallation.class);

        when(ParseInstallation.getCurrentInstallation()).thenReturn(INSTALLATION);
    }

    @Test
    public void shouldCallParseCloudWithCorrectAttributes() {
        InvitePlayersOperation operation = new InvitePlayersOperation();

        Object retVal = operation.run();
        assertOperationReturnValue(retVal);

        HashMap<String, Object> expectedParams = new HashMap<>();
        expectedParams.put("installationId", INSTALLATION_ID);

        assertOperationParams(InvitePlayersOperation.INVITE_PLAYERS_FUNCTION_NAME, expectedParams);
    }
}
