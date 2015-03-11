package com.iic.mokojin.presenters;

import com.iic.mokojin.models.Match;

import java.text.DecimalFormat;

/**
 * Created by yon on 3/10/15.
 */
public class MatchPresenter {

    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("##.##");

    public static String getRatioString(Match match){
        double chanceA = match.getChanceToWin();
        double chanceB = 1.0 - chanceA;
        if (chanceA > chanceB) {
            return String.format("%s : 1", DECIMAL_FORMAT.format(chanceA/chanceB));
        } else {
            return String.format("1 : %s", DECIMAL_FORMAT.format(chanceB/chanceA));
        }
    }

    public static int getProgress(Match match){
        return (int) Math.round(100 * match.getChanceToWin());
    }
}
