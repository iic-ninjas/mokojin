package com.iic.mokojin;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iic.mokojin.models.Match;
import com.iic.mokojin.models.Player;

import bolts.Continuation;
import bolts.Task;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A placeholder fragment containing a simple view.
 */
public class CurrentMatchFragment extends Fragment {

    private static final String LOG_TAG = CurrentMatchFragment.class.getName();

    private Match mCurrentMatch;

    public CurrentMatchFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_current_match, container, false);
        ButterKnife.inject(this, rootView);

        Match.getCurrent().continueWith(new Continuation<Match, Void>() {
            @Override
            public Void then(Task<Match> task) throws Exception {
                if (task.isCancelled()) {
                    Log.v(LOG_TAG, "Fetching of current match was cancelled");
                }
                else if (task.isFaulted()) {
                    Log.e(LOG_TAG, "Error fetching current match", task.getError());
                } else {
                    mCurrentMatch = task.getResult();
                    refreshUI();
                }
                return null;
            }
        }, Task.UI_THREAD_EXECUTOR);

        return rootView;
    }

    @InjectView(R.id.player_a_name)  TextView mPlayerANameTextView;
    @InjectView(R.id.player_a_image) ImageView mPlayerAImageView;
    @InjectView(R.id.player_b_name)  TextView mPlayerBNameTextView;
    @InjectView(R.id.player_b_image) ImageView mPlayerBImageView;

    private int getDrawableId(int characterId) {
        return getResources().getIdentifier("player_" + characterId, "drawable", this.getActivity().getPackageName());
    }

    private void setPlayerImages(Player player, ImageView charA, ImageView charB) {
        if (player.getCharacterA() != null) {
            int characterId = player.getCharacterA().getCharacterId();
            Drawable charDrawable = getResources().getDrawable(getDrawableId(characterId));
            charA.setImageDrawable(charDrawable);
        } else {
            charA.setImageDrawable(getResources().getDrawable(R.drawable.player_empty));
        }
    }

    private void refreshUI() {

        if (mCurrentMatch != null) {
            mPlayerANameTextView.setText(mCurrentMatch.getPlayerA().getPerson().getName());
            mPlayerBNameTextView.setText(mCurrentMatch.getPlayerB().getPerson().getName());

            setPlayerImages(mCurrentMatch.getPlayerA(), mPlayerAImageView, null);
            setPlayerImages(mCurrentMatch.getPlayerB(), mPlayerBImageView, null);
        }
    }
}
