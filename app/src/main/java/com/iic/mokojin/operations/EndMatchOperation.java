package com.iic.mokojin.operations;

import com.iic.mokojin.models.Match;
import com.iic.mokojin.models.Player;
import com.parse.ParseCloud;

import java.util.HashMap;

import bolts.Task;

/**
 * Created by giladgo on 3/3/15.
 */
public class EndMatchOperation {
    public static final String END_MATCH_CLOUD_FUNCTION_NAME = "endMatch";
    private Match mMatch;
    private Player.PlayerType mWinner;

    private static final HashMap<Player.PlayerType, String> mPlayerTypeToString;

    static {
        mPlayerTypeToString = new HashMap<>();
        mPlayerTypeToString.put(Player.PlayerType.PLAYER_A, "PlayerA");
        mPlayerTypeToString.put(Player.PlayerType.PLAYER_B, "PlayerB");
    }

    public EndMatchOperation(Match match, Player.PlayerType winner) {
        mMatch = match;
        mWinner = winner;
    }

    public Task<Match> run(){
        HashMap<String, Object> params = buildParams(mMatch, mWinner);
        return ParseCloud.callFunctionInBackground(END_MATCH_CLOUD_FUNCTION_NAME, params);
    }

    private HashMap<String, Object> buildParams(Match match, Player.PlayerType winner){
        HashMap<String, Object> params = new HashMap<>();
        params.put("match",  match.getObjectId());
        params.put("winner", mPlayerTypeToString.get(winner));

        return params;
    }

}
