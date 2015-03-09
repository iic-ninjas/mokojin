package com.iic.mokojin.operations;

import com.iic.mokojin.models.Person;
import com.iic.mokojin.models.Player;
import com.parse.ParseCloud;

import java.util.HashMap;

import bolts.Task;

/**
 * Created by giladgo on 3/5/15.
 */
public class JoinQueueOperation {

    public static final String JOIN_QUEUE_CLOUD_FUNCTION_NAME = "joinQueue";

    public Task<Player> run(Person person){
        HashMap<String, Object> params = buildParams(person);
        return ParseCloud.callFunctionInBackground(JOIN_QUEUE_CLOUD_FUNCTION_NAME, params);
    }

    private HashMap<String, Object> buildParams(Person person){
        HashMap<String, Object> params = new HashMap<>();
        params.put("person",  person.getObjectId());
        return params;
    }
}
