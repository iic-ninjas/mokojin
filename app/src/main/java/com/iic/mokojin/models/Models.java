package com.iic.mokojin.models;

import com.parse.ParseException;
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

    public static <T extends ParseObject> void saveToLocalStorage(T object) {
        try {
            object.pin();
        } catch (ParseException ignored) {}
    }

    public static <T extends ParseObject> T loadFromLocalStorage(Class<T> cls, String objectId) {
        try{
            T object = ParseObject.createWithoutData(cls, objectId);
            object.fetchFromLocalDatastore();
            return object;
        } catch (ParseException ex) {
            return null;
        }
    }


}
