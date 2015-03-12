package com.iic.mokojin.cloud.getters;

import com.iic.mokojin.models.Character;
import com.parse.ParseCloud;

import java.util.HashMap;
import java.util.List;

import bolts.Task;

/**
 * Created by giladgo on 3/10/15.
 */
public class GetCharacters {

    private static final String GET_PEOPLE_CLOUD_FUNCTION_NAME = "getCharacters";

    public static Task<List<Character>> getCharacters() {
        return ParseCloud.callFunctionInBackground(GET_PEOPLE_CLOUD_FUNCTION_NAME, new HashMap<String, Object>());
    }
}
