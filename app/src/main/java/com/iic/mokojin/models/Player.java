package com.iic.mokojin.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;

/**
 * Created by giladgo on 3/3/15.
 */
@ParseClassName("Player")
public class Player extends ParseObject implements Parcelable{

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

    // Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        try {
            this.pin();
        }
        catch (ParseException ignored) {}
        dest.writeString(getObjectId());
    }

    public Player() {
        super();
    }


    public static final Parcelable.Creator<Player> CREATOR = new Parcelable.Creator<Player>() {

        public Player createFromParcel(Parcel in) {
            Player player = ParseObject.createWithoutData(Player.class, in.readString());
            try {
                player.fetchFromLocalDatastore();
            }
            catch (ParseException ignored) {}
            return player;
        }

        public Player[] newArray(int size) {
            return new Player[size];
        }
    };
}
