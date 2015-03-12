package com.iic.mokojin.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
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
    private Direction mDirection = Direction.left;
    private Size mSize = Size.big;
    private int mMargin;
    private float mBorderWidth;

    static enum Direction {
        left,
        right
    }
    static enum Size {
        big,
        small
    }

    public CharacterViewer(Context context) {
        super(context);
        init();
    }

    public CharacterViewer(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray attributes = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CharacterViewer,
                0, 0);

        try {
            mDirection = Direction.values()[attributes.getInt(R.styleable.CharacterViewer_direction, Direction.left.ordinal())];
            mSize = Size.values()[attributes.getInt(R.styleable.CharacterViewer_size, Size.big.ordinal())];
        } finally {
            attributes.recycle();
        }

        init();
    }

    public CharacterViewer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.character_viewer, this);

        ButterKnife.inject(this);
        if (mSize == Size.small){
            mFrontImage.setBorderColor(getResources().getColor(R.color.background));
            shrinkView(mFrontImage);
            shrinkView(mBackImage);
            mMargin = getResources().getDimensionPixelSize(R.dimen.smaller_character_separation_amount);
            mBorderWidth = getResources().getDimension(R.dimen.smaller_character_border_width);
        } else {
            mMargin = getResources().getDimensionPixelSize(R.dimen.character_separation_amount);
            mBorderWidth = getResources().getDimension(R.dimen.character_border_width);
        }
    }

    private void shrinkView(RoundedImageView imageView) {
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        layoutParams.height = getResources().getDimensionPixelSize(R.dimen.small_avatar_size);
        layoutParams.width = getResources().getDimensionPixelSize(R.dimen.small_avatar_size);
        imageView.setLayoutParams(layoutParams);
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

        if (mDirection == Direction.left){
            setMarginLeft(mFrontImage, 0);
            setMarginLeft(mBackImage, mMargin);
        } else {
            setMarginLeft(mBackImage, 0);
            setMarginLeft(mFrontImage, mMargin);
        }
        mFrontImage.setBorderWidth(mBorderWidth);
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
