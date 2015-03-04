package com.iic.mokojin.models;

import com.parse.ParseObject;

/**
 * Created by giladgo on 3/3/15.
 */
public final class Models {

    public static void registerModels() {

        // Register all your models here
        ParseObject.registerSubclass(Match.class);
        ParseObject.registerSubclass(Character.class);
        ParseObject.registerSubclass(Player.class);
        ParseObject.registerSubclass(Person.class);
        ParseObject.registerSubclass(QueueItem.class);
    }




}
