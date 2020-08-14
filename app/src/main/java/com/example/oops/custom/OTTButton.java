package com.example.oops.custom;

import android.content.Context;
import android.graphics.Typeface;

import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;



public class OTTButton extends AppCompatButton {
    public OTTButton(Context context) {
        super(context);

        applyCustomFont(context);
    }

    public OTTButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context);
    }

    public OTTButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont= FontCache.getTypeface(context,"futura light bt.ttf");
        setTypeface(customFont);
    }
}


