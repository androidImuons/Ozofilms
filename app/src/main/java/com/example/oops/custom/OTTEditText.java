package com.example.oops.custom;

import android.content.Context;
import android.graphics.Typeface;

import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

/**
 * Created by sunil on 27-03-2018.
 */

public class OTTEditText extends AppCompatEditText {
    public OTTEditText(Context context) {
        super(context);

        applyCustomFont(context);
    }

    public OTTEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context);
    }

    public OTTEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont= FontCache.getTypeface(context,"futura light bt.ttf");
        setTypeface(customFont);
    }
}

