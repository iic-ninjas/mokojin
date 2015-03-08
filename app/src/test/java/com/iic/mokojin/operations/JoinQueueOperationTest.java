package com.iic.mokojin.operations;

import com.iic.mokojin.models.Person;
import com.iic.mokojin.utils.MockFactory;

import org.junit.Test;

import java.util.HashMap;

/**
 * Created by yon on 3/4/15.
 */
public class JoinQueueOperationTest extends AbstractOperationTest {

    public static final String PERSON_ID = "PLAYER_ID";
    public static final Person PERSON = MockFactory.createPerson(PERSON_ID);

    @Test
    public void shouldCallParseCloudWithCorrectAttributes(){
        JoinQueueOperation operation = new JoinQueueOperation();

        Object retVal = operation.run(PERSON);
        assertOperationReturnValue(retVal);

        HashMap<String, Object> expectedParams = new HashMap<>();
        expectedParams.put("person", PERSON_ID);

        assertOperationParams(JoinQueueOperation.JOIN_QUEUE_CLOUD_FUNCTION_NAME, expectedParams);

    }
    
}
