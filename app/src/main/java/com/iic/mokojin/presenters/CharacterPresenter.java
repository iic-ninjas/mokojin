package com.iic.mokojin.presenters;

import android.content.Context;

import com.iic.mokojin.R;

/**
 * Created by yon on 3/3/15.
 */
public class CharacterPresenter {
    
    public static final int DEFAULT_IMAGE_RESOURCE = R.drawable.player_0;
    
    public static int getImageResource(Context context, com.iic.mokojin.models.Character character){
        if (character == null) return DEFAULT_IMAGE_RESOURCE;
        String resourceId = "player_".concat(String.valueOf(character.getCharacterId()));
        String packageName = context.getPackageName();
        packageName = "com.iic.mokojin";
        return context.getResources().getIdentifier(resourceId, "drawable", packageName);
    }

}
