package com.iic.mokojin;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.iic.mokojin.models.Match;
import com.iic.mokojin.models.Player;
import com.iic.mokojin.operation.EndMatchOperation;

import bolts.Continuation;
import bolts.Task;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnLongClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class CurrentMatchFragment extends Fragment {

    private static final String LOG_TAG = CurrentMatchFragment.class.getName();

    private Match mCurrentMatch;

    @InjectView(R.id.empty_text_view)  View mEmptyText;
    @InjectView(R.id.players_container) View mPlayersContainer;

    @InjectView(R.id.player_a_name)  TextView mPlayerANameTextView;
    @InjectView(R.id.player_a_image_front) ImageView mPlayerAImageViewFront;
    @InjectView(R.id.player_a_image_back) ImageView mPlayerAImageViewBack;

    @InjectView(R.id.player_b_name)  TextView mPlayerBNameTextView;
    @InjectView(R.id.player_b_image_front) ImageView mPlayerBImageViewFront;
    @InjectView(R.id.player_b_image_back) ImageView mPlayerBImageViewBack;

    public CurrentMatchFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_current_match, container, false);
        ButterKnife.inject(this, rootView);

        return rootView;
    }

    private void refreshCurrentMatch() {
        mCurrentMatch = null;
        Match.getCurrent().continueWith(new Continuation<Match, Void>() {
            @Override
            public Void then(Task<Match> task) throws Exception {
                if (task.isCancelled()) {
                    Log.d(LOG_TAG, "Fetching of current match was cancelled");
                } else if (task.isFaulted()) {
                    Log.e(LOG_TAG, "Error fetching current match", task.getError());
                } else {
                    mCurrentMatch = task.getResult();
                    refreshUI();
                }
                return null;
            }
        }, Task.UI_THREAD_EXECUTOR);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshCurrentMatch();
    }

    private void displayCharacter(int characterId, ImageView imageView) {
        int resourceId = getResources().getIdentifier("player_" + characterId, "drawable", this.getActivity().getPackageName());
        Drawable charDrawable = getResources().getDrawable(resourceId);
        imageView.setImageDrawable(charDrawable);
    }

    private void hideBackImage(ImageView frontImageView, ImageView backImageView) {
        backImageView.setVisibility(View.GONE);

        // Get rid of margins and put it in the middle
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)frontImageView.getLayoutParams();
        layoutParams.rightMargin = 0;
        layoutParams.topMargin = 0;
        layoutParams.gravity = Gravity.CENTER;
        frontImageView.setLayoutParams(layoutParams);
    }

    private void setPlayerImages(Player player, ImageView charA, ImageView charB) {
        if (player.getCharacterA() != null && player.getCharacterB() != null) {
            displayCharacter(player.getCharacterA().getCharacterId(), charA);
            displayCharacter(player.getCharacterB().getCharacterId(), charB);
        } else if (player.getCharacterA() != null) {
            displayCharacter(player.getCharacterA().getCharacterId(), charA);
            hideBackImage(charA, charB);
        } else {
            charA.setImageDrawable(getResources().getDrawable(R.drawable.player_empty));
            hideBackImage(charA, charB);
        }
    }

    private void refreshUI() {
        if (mCurrentMatch != null) {
            mEmptyText.setVisibility(View.INVISIBLE);
            mPlayersContainer.setVisibility(View.VISIBLE);

            mPlayerANameTextView.setText(mCurrentMatch.getPlayerA().getPerson().getName());
            mPlayerBNameTextView.setText(mCurrentMatch.getPlayerB().getPerson().getName());

            setPlayerImages(mCurrentMatch.getPlayerA(), mPlayerAImageViewFront, mPlayerAImageViewBack);
            setPlayerImages(mCurrentMatch.getPlayerB(), mPlayerBImageViewFront, mPlayerBImageViewBack);
        } else {
            mEmptyText.setVisibility(View.VISIBLE);
            mPlayersContainer.setVisibility(View.INVISIBLE);
        }
    }

    private void endMatch(Player.PlayerType playerType) {
        new EndMatchOperation(mCurrentMatch, playerType).run().continueWith(new Continuation<Match, Void>() {
            @Override
            public Void then(Task<Match> task) throws Exception {
                refreshCurrentMatch();
                return null;
            }
        }, Task.UI_THREAD_EXECUTOR);
    }

    @SuppressWarnings("unused")
    @OnClick({R.id.player_a_image_front, R.id.player_a_image_back})
    void onPlayerAClick() {
        endMatch(Player.PlayerType.PLAYER_A);
    }

    @SuppressWarnings("unused")
    @OnClick({R.id.player_b_image_front, R.id.player_b_image_back})
    void onPlayerBClick() {
        endMatch(Player.PlayerType.PLAYER_B);
    }

    @SuppressWarnings("unused")
    @OnLongClick({R.id.player_a_image_front, R.id.player_a_image_back})
    boolean onPlayerALongClick() {
        // TODO: open the character selector activity
        return true;
    }

    @SuppressWarnings("unused")
    @OnLongClick({R.id.player_b_image_front, R.id.player_b_image_back})
    boolean onPlayerBLongClick() {
        // TODO: open the character selector activity
        return true;
    }
}
