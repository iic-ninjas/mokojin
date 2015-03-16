package com.iic.mokojin.cloud.operations;

import com.parse.ParseCloud;
import com.parse.ParseInstallation;

import java.util.HashMap;

import bolts.Task;

/**
 * Created by udi on 3/12/15.
 */
public class InvitePlayersOperation {

    public static final String INVITE_PLAYERS_FUNCTION_NAME = "invitePlayers";

    public Task<Void> run(){
        HashMap<String, Object> params = buildParams();
        return ParseCloud.callFunctionInBackground(INVITE_PLAYERS_FUNCTION_NAME, params);
    }

    private HashMap<String, Object> buildParams(){
        HashMap<String, Object> params = new HashMap<>();
        params.put("installationId", ParseInstallation.getCurrentInstallation().getInstallationId());
        return params;
    }
}
