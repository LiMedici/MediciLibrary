package com.medici.stack.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * **********************************

 * @function 跑马灯效果的TextView
 *
 * **********************************
 */
public class FocusableTextView extends AppCompatTextView {
    public FocusableTextView(Context context) {
        super(context);
    }

    public FocusableTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FocusableTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
