package com.iic.mokojin.cloud.operations;

import com.iic.mokojin.models.QueueItem;
import com.parse.ParseCloud;

import java.util.HashMap;

import bolts.Task;

/**
 * Created by udi on 3/5/15.
 */
public class LeaveQueueOperation {
    public static final String LEAVE_QUEUE_CLOUD_FUNCTION_NAME = "leaveQueue";

    public Task<Void> run(QueueItem queueItem){
        HashMap<String, Object> params = buildParams(queueItem);
        return ParseCloud.callFunctionInBackground(LEAVE_QUEUE_CLOUD_FUNCTION_NAME, params);
    }

    private HashMap<String, Object> buildParams(QueueItem queueItem){
        HashMap<String, Object> params = new HashMap<>();
        params.put("queueItem",  queueItem.getObjectId());
        return params;
    }
}
