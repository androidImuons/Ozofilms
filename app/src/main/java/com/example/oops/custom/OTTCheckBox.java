package com.example.oops.custom;

import android.content.Context;
import android.graphics.Typeface;

import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatCheckBox;

/**
 * Created by sunil on 25-04-2018.
 */

public class OTTCheckBox extends AppCompatCheckBox {
    public OTTCheckBox(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public OTTCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context);
    }
    public OTTCheckBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context);
    }
    private void applyCustomFont(Context context) {
        Typeface customFont= FontCache.getTypeface(context,"futura_n.ttf");
        setTypeface(customFont);
    }
}
