package com.iic.mokojin.operations;

import com.parse.ParseCloud;

import java.util.HashMap;

import bolts.Task;

/**
 * Created by yon on 3/11/15.
 */
public class GoodNightOperation {
    public static final String GOOD_NIGHT_OPERATION_NAME = "goodnight";

    public Task<Void> run(){
        HashMap<String, Object> params = new HashMap<>();
        return ParseCloud.callFunctionInBackground(GOOD_NIGHT_OPERATION_NAME, params);
    }

}
