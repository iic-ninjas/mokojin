package com.iic.mokojin.cloud.operations;

import com.iic.mokojin.models.QueueItem;
import com.iic.mokojin.utils.MockFactory;

import org.junit.Test;

import java.util.HashMap;

/**
 * Created by udi on 3/11/15.
 */
public class LeaveQueueOperationTest extends AbstractOperationTest {

    public static final String QUEUE_ITEM_ID = "QUEUE_ITEM_ID";
    public static final QueueItem QUEUE_ITEM = MockFactory.createQueueItem(QUEUE_ITEM_ID);

    @Test
    public void shouldCallParseCloudWithCorrectAttributes(){
        LeaveQueueOperation operation = new LeaveQueueOperation();

        Object retVal = operation.run(QUEUE_ITEM);
        assertOperationReturnValue(retVal);

        HashMap<String, Object> expectedParams = new HashMap<>();
        expectedParams.put("queueItem", QUEUE_ITEM_ID);

        assertOperationParams(LeaveQueueOperation.LEAVE_QUEUE_CLOUD_FUNCTION_NAME, expectedParams);

    }
}
