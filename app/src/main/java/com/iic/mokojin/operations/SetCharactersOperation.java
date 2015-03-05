package com.iic.mokojin.operations;

import com.iic.mokojin.models.Player;
import com.parse.ParseCloud;

import java.util.HashMap;

import bolts.Task;

/**
 * Created by giladgo on 3/3/15.
 */
public class SetCharactersOperation  {
    
    public static final String SET_CHARACTER_CLOUD_FUNCTION_NAME = "setCharacter";

    public Task<Player> run(Player player, com.iic.mokojin.models.Character characterA, com.iic.mokojin.models.Character characterB){
        HashMap<String, Object> params = buildParams(player, characterA, characterB);
        return ParseCloud.callFunctionInBackground(SET_CHARACTER_CLOUD_FUNCTION_NAME, params);
    }

    private HashMap<String, Object> buildParams(Player player, com.iic.mokojin.models.Character characterA, com.iic.mokojin.models.Character characterB){
        HashMap<String, Object> params = new HashMap<>();
        params.put("player",  player.getObjectId());
        params.put("characterA", characterA.getObjectId());
        params.put("characterB", null != characterB ? characterB.getObjectId() : null);
        return params;
    }
}
