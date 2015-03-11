package com.iic.mokojin.operations;

import com.iic.mokojin.models.Person;
import com.parse.ParseCloud;

import java.util.HashMap;

import bolts.Task;

/**
 * Created by giladgo on 3/5/15.
 */
public class CreatePersonOperation {

    public static final String CREATE_PERSON_CLOUD_FUNCTION_NAME = "createPerson";

    public Task<Person> run(String name){
        HashMap<String, Object> params = buildParams(name);
        return ParseCloud.callFunctionInBackground(CREATE_PERSON_CLOUD_FUNCTION_NAME, params);
    }

    private HashMap<String, Object> buildParams(String name){
        HashMap<String, Object> params = new HashMap<>();
        params.put("name",  name);
        return params;
    }
}
