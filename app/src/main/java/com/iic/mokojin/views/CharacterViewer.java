package com.iic.mokojin.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.iic.mokojin.R;
import com.iic.mokojin.models.Player;
import com.iic.mokojin.presenters.CharacterPresenter;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by giladgo on 3/4/15.
 */
public class CharacterViewer extends FrameLayout {

    private Player mPlayer;
    @InjectView(R.id.character_image_front) ImageView mFrontImage;
    @InjectView(R.id.character_image_back) ImageView mBackImage;

    public CharacterViewer(Context context) {
        super(context);
        init();
    }

    public CharacterViewer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CharacterViewer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.character_viewer, this);
        if (!isInEditMode()) ButterKnife.inject(this);
    }

    public void setPlayer(Player player) {
        mPlayer = player;
        refreshUI();
    }

    private void hideBackImage() {
        mBackImage.setVisibility(View.GONE);

        // Get rid of margins and put it in the middle
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)mFrontImage.getLayoutParams();
        layoutParams.rightMargin = 0;
        layoutParams.topMargin = 0;
        layoutParams.gravity = Gravity.CENTER;
        mFrontImage.setLayoutParams(layoutParams);
    }

    private void showBackImage() {
        mBackImage.setVisibility(View.VISIBLE);

        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)mFrontImage.getLayoutParams();
        layoutParams.rightMargin = getResources().getDimensionPixelSize(R.dimen.character_separation_amount);
        layoutParams.topMargin = getResources().getDimensionPixelSize(R.dimen.character_separation_amount);
        layoutParams.gravity = Gravity.NO_GRAVITY;
        mFrontImage.setLayoutParams(layoutParams);
    }

    private void refreshUI() {
        mFrontImage.setImageResource(CharacterPresenter.getImageResource(getContext(), mPlayer.getCharacterA()));
        mBackImage.setImageResource(CharacterPresenter.getImageResource(getContext(), mPlayer.getCharacterB()));

        if (mPlayer.getCharacterA() != null && mPlayer.getCharacterB() != null) {
            showBackImage();
        } else {
            hideBackImage();
        }
    }

}
