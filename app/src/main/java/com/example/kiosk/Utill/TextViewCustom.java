package com.example.kiosk.Utill;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class TextViewCustom extends AppCompatTextView {
    public TextViewCustom(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public TextViewCustom(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextViewCustom(Context context) {
        super(context);
        init();
    }

    public void init() {
        setTypeface(Typeface.createFromAsset(getContext().getAssets(), "font/RUBIK-LIGHT.TTF"), 1);
    }
}
