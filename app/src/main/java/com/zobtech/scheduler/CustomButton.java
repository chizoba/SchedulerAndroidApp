package com.zobtech.scheduler;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by Junior on 15/06/2015.
 */
public class CustomButton extends Button {
    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Lato-Heavy.ttf"));
    }
}
