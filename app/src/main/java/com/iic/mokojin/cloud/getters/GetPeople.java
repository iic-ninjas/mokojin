package com.iic.mokojin.cloud.getters;

import com.iic.mokojin.models.Person;
import com.parse.ParseCloud;

import java.util.HashMap;
import java.util.List;

import bolts.Task;

/**
 * Created by giladgo on 3/10/15.
 */
public class GetPeople {

    private static final String GET_PEOPLE_CLOUD_FUNCTION_NAME = "getPeople";

    public static Task<List<Person>> getPeople() {
        return ParseCloud.callFunctionInBackground(GET_PEOPLE_CLOUD_FUNCTION_NAME, new HashMap<String, Object>());
    }
}
