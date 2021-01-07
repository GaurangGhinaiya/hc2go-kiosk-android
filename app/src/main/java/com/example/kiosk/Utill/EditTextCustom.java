package com.example.kiosk.Utill;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class EditTextCustom extends androidx.appcompat.widget.AppCompatEditText {
    public EditTextCustom(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    public EditTextCustom(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public EditTextCustom(Context context) {
        super(context);
        init();
    }
    public void init() {
        setTypeface(Typeface.createFromAsset(getContext().getAssets(), "font/RUBIK-LIGHT.TTF"), 1);
    }
}
