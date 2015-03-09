package com.iic.mokojin.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.iic.mokojin.R;
import com.iic.mokojin.models.Player;
import com.iic.mokojin.presenters.CharacterPresenter;
import com.makeramen.roundedimageview.RoundedImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by giladgo on 3/4/15.
 */
public class CharacterViewer extends FrameLayout {

    private Player mPlayer;
    @InjectView(R.id.character_image_front) RoundedImageView mFrontImage;
    @InjectView(R.id.character_image_back) RoundedImageView mBackImage;
    private int mDirection;

    static enum Direction {
        left,
        right
    }

    public CharacterViewer(Context context) {
        super(context);
        init();
    }

    public CharacterViewer(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CharacterViewer,
                0, 0);

        try {
            mDirection = a.getInt(R.styleable.CharacterViewer_direction, Direction.left.ordinal());
        } finally {
            a.recycle();
        }

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
        layoutParams.leftMargin = 0;
        layoutParams.gravity = Gravity.CENTER;
        mFrontImage.setLayoutParams(layoutParams);
        mFrontImage.setBorderWidth(0.0f);

    }

    private void showBackImage() {
        mBackImage.setVisibility(View.VISIBLE);

        if (mDirection == Direction.left.ordinal()){
            setMarginLeft(mFrontImage, 0);
            setMarginLeft(mBackImage, getResources().getDimensionPixelSize(R.dimen.character_separation_amount));
        } else {
            setMarginLeft(mBackImage, 0);
            setMarginLeft(mFrontImage, getResources().getDimensionPixelSize(R.dimen.character_separation_amount));
        }
        mFrontImage.setBorderWidth(getResources().getDimension(R.dimen.character_border_width));
    }
    
    private void setMarginLeft(View view, int amount){
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)view.getLayoutParams();
        layoutParams.leftMargin = amount;
        layoutParams.gravity = Gravity.NO_GRAVITY;
        view.setLayoutParams(layoutParams);
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
