package com.example.kiosk.Utill;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class TextViewCustomBold extends AppCompatTextView {
    public TextViewCustomBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public TextViewCustomBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextViewCustomBold(Context context) {
        super(context);
        init();
    }

    public void init() {
        setTypeface(Typeface.createFromAsset(getContext().getAssets(), "font/RUBIK-MEDIUM.TTF"), 1);
    }
}
