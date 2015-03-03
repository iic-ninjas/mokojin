package com.iic.mokojin.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by giladgo on 3/3/15.
 */
@ParseClassName("Person")
public class Person extends ParseObject{
    public String getAvatarUrl() {
        return getString("avatarUrl");
    }

    public String getName() {
        return getString("name");
    }

    public String getDeviceToken() {
        return getString("deviceToken");
    }

}