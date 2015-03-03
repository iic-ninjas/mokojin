package com.iic.mokojin.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by giladgo on 3/3/15.
 */
@ParseClassName("Character")
public class Character extends ParseObject {

    public int getCharacterId() {
        return getInt("characterId");
    }

    public String getAvatarUrl() {
        return getString("avatarUrl");
    }

    public String getName() {
        return getString("name");
    }
}
