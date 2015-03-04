package com.iic.mokojin.presenters;

import android.content.Context;

/**
 * Created by yon on 3/3/15.
 */
public class CharacterPresenter {
    
    public static int getImageResource(Context context, com.iic.mokojin.models.Character character){
        String resourceId = "player_".concat(String.valueOf(character.getCharacterId()));
        return context.getResources().getIdentifier(resourceId, "drawable", context.getPackageName() );
    }

}
