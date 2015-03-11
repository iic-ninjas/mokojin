package com.iic.mokojin.cloud.getters;

import com.iic.mokojin.models.Character;
import com.parse.ParseCloud;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import bolts.Continuation;
import bolts.Task;

/**
 * Created by giladgo on 3/10/15.
 */
public class GetCharacters {

    private static final String GET_PEOPLE_CLOUD_FUNCTION_NAME = "getCharacters";

    public static Task<List<com.iic.mokojin.models.Character>> getCharacters() {
        return ParseCloud.<List<Object>>callFunctionInBackground(GET_PEOPLE_CLOUD_FUNCTION_NAME, new HashMap<String, Object>())
                .onSuccess(new Continuation<List<Object>, List<com.iic.mokojin.models.Character>>() {
                    @Override
                    public List<com.iic.mokojin.models.Character> then(Task<List<Object>> task) throws Exception {
                        List<Object> objectList = task.getResult();

                        List<com.iic.mokojin.models.Character> characterList = new ArrayList<>();
                        for (Object character : objectList) {
                            characterList.add((Character) character);
                        }

                        return characterList;
                    }
                });
    }
}
