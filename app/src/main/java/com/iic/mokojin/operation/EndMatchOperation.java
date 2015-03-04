package com.iic.mokojin.operation;

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

        if (winner == Player.PlayerType.PLAYER_A) {
            params.put("winner", "PlayerA");
        } else if (winner == Player.PlayerType.PLAYER_B) {
            params.put("winner", "PlayerB");
        }

        return params;
    }

}
