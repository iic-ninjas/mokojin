package com.iic.mokojin.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by giladgo on 3/3/15.
 */
@ParseClassName("Player")
public class Player extends ParseObject implements Parcelable{

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
        dest.writeString(getObjectId());
//        dest.writeParcelable(getPerson(), flags);
//        dest.writeParcelable(getCharacterA(), flags);
//        dest.writeParcelable(getCharacterB(), flags);
    }

    public Player() {
        super();
    }
    
    public Player(Parcel source) {
        setObjectId(source.readString());
        put("person", source.readParcelable(null));
        put("characterA", source.readParcelable(null));
        put("characterB", source.readParcelable(null));
    }

    public static final Parcelable.Creator<Player> CREATOR = new Parcelable.Creator<Player>() {

        public Player createFromParcel(Parcel in) {
            return new Player(in);
        }

        public Player[] newArray(int size) {
            return new Player[size];
        }
    };
}
