package com.iic.mokojin.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Date;

import bolts.Task;

/**
 * Created by giladgo on 3/3/15.
 */
@ParseClassName("Match")
public class Match extends ParseObject {

    public Match() {
    }

    public String getWinner() {
        return getString("winner");
    }

    public Date getStartTime() {
        return getDate("startTime");
    }

    public Date getEndTime() {
        return getDate("endTime");
    }
    
    public double getChanceToWin() { return getDouble("expected"); }
    
    public Player getPlayerA() {
        return (Player)getParseObject("playerA");
    }

    public Player getPlayerB() {
        return (Player)getParseObject("playerB");
    }

    public static Task<Match> getCurrent() {
        ParseQuery<Match> parseQuery = ParseQuery.getQuery(Match.class);
        parseQuery.whereDoesNotExist("winner");
        parseQuery.whereDoesNotExist("endTime");

        parseQuery.include("playerA.characterA");
        parseQuery.include("playerB.characterA");
        parseQuery.include("playerA.characterB");
        parseQuery.include("playerB.characterB");

        parseQuery.include("playerA.person");
        parseQuery.include("playerB.person");

        return parseQuery.getFirstInBackground();

    }

}

