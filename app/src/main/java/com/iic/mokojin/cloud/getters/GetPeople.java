package com.iic.mokojin.cloud.getters;

import com.iic.mokojin.models.Person;
import com.parse.ParseCloud;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import bolts.Continuation;
import bolts.Task;

/**
 * Created by giladgo on 3/10/15.
 */
public class GetPeople {

    private static final String GET_PEOPLE_CLOUD_FUNCTION_NAME = "getPeople";

    public static Task<List<Person>> getPeople() {
        return ParseCloud.<List<Object>>callFunctionInBackground(GET_PEOPLE_CLOUD_FUNCTION_NAME, new HashMap<String, Object>())
                .onSuccess(new Continuation<List<Object>, List<Person>>() {
            @Override
            public List<Person> then(Task<List<Object>> task) throws Exception {
                List<Object> objectList = task.getResult();

                List<Person> peopleList = new ArrayList<>();
                for (Object person : objectList) {
                    peopleList.add((Person)person);
                }

                return peopleList;
            }
        });
    }
}
