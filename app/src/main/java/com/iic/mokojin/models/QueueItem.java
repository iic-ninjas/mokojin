package com.iic.mokojin.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by udi on 3/4/15.
 */

@ParseClassName("QueueItem")
public class QueueItem extends ParseObject {
    public Player getPlayer() { return (Player) getParseObject("player"); }
}
