package com.iic.mokojin.cloud.operations;

import com.iic.mokojin.models.Character;
import com.iic.mokojin.models.Player;
import com.parse.ParseCloud;

import java.util.HashMap;

import bolts.Task;

/**
 * Created by giladgo on 3/3/15.
 */
public class SetCharactersOperation  {
    
    public static final String SET_CHARACTER_CLOUD_FUNCTION_NAME = "setCharacter";

    public Task<Player> run(Player player, Character characterA, Character characterB){
        HashMap<String, Object> params = buildParams(player, characterA, characterB);
        return ParseCloud.callFunctionInBackground(SET_CHARACTER_CLOUD_FUNCTION_NAME, params);
    }

    private HashMap<String, Object> buildParams(Player player, Character characterA, Character characterB){
        HashMap<String, Object> params = new HashMap<>();
        params.put("player",  player.getObjectId());
        params.put("characterA", characterA.getObjectId());
        params.put("characterB", null != characterB ? characterB.getObjectId() : null);
        return params;
    }
}
