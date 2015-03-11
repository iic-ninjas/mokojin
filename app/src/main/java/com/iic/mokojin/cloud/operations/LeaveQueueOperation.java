package com.iic.mokojin.cloud.operations;

import com.iic.mokojin.models.QueueItem;
import com.parse.ParseCloud;
import com.parse.ParseException;

import java.util.HashMap;

/**
 * Created by udi on 3/5/15.
 */
public class LeaveQueueOperation {
    public static final String LEAVE_QUEUE_CLOUD_FUNCTION_NAME = "leaveQueue";

    public void run(QueueItem queueItem) throws ParseException {
        HashMap<String, Object> params = buildParams(queueItem);
        ParseCloud.callFunction(LEAVE_QUEUE_CLOUD_FUNCTION_NAME, params);
        return;
    }

    private HashMap<String, Object> buildParams(QueueItem queueItem){
        HashMap<String, Object> params = new HashMap<>();
        params.put("queueItem",  queueItem.getObjectId());
        return params;
    }
}
