package com.iic.mokojin.operations;

import org.junit.Test;

import java.util.HashMap;

/**
 * Created by giladgo on 3/8/15.
 */
public class CreatePersonOperationTest extends AbstractOperationTest {
    public static final String PERSON_NAME = "moshe";

    @Test
    public void shouldCallParseCloudWithCorrectAttributes(){
        CreatePersonOperation operation = new CreatePersonOperation();

        Object retVal = operation.run(PERSON_NAME);
        assertOperationReturnValue(retVal);

        HashMap<String, Object> expectedParams = new HashMap<>();
        expectedParams.put("name", PERSON_NAME);

        assertOperationParams(CreatePersonOperation.CREATE_PERSON_CLOUD_FUNCTION_NAME, expectedParams);

    }
}
