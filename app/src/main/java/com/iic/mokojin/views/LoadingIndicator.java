package com.iic.mokojin.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.iic.mokojin.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by ggoldberg on 3/18/15.
 */
public class LoadingIndicator extends FrameLayout {

    @InjectView(R.id.progress_text)
    TextView mTextView;

    public LoadingIndicator(Context context) {
        super(context);
        init(null);
    }

    public LoadingIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public LoadingIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        inflate(getContext(), R.layout.loading_indicator, this);

        ButterKnife.inject(this);

        if (attrs != null) {
            TypedArray a = null;
            try {
                a = getContext().obtainStyledAttributes(attrs, new int[]{android.R.attr.text});
                setText(a.getText(0));
            }
            finally {
                if (a != null) {
                    a.recycle();
                }

            }
        }
    }

    public CharSequence getText() {
        return mTextView.getText();
    }

    public void setText(CharSequence text) {
        mTextView.setText(text);
    }
}