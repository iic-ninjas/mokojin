package com.iic.mokojin.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by giladgo on 3/3/15.
 */
@ParseClassName("Player")
public class Player extends ParseObject {

    public static enum PlayerType {
        PLAYER_A,
        PLAYER_B
    }

    public Person getPerson() {
        return (Person)getParseObject("person");
    }

    public Character getCharacterA() {
        return (Character)getParseObject("characterA");
    }

    public Character getCharacterB() {
        return (Character)getParseObject("characterB");
    }

}
