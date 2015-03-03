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

    public Player getPlayerA() {
        return (Player)getParseObject("playerA");
    }

    public Player getPlayerB() {
        return (Player)getParseObject("playerB");
    }

    public void putWinner(String winner) {
        put("winner", winner);
    }

    public void putStartTime(Date startTime) {
        put("startTime", startTime);
    }

    public void putEndTime(Date endTime) {
        put("endTime", endTime);
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

