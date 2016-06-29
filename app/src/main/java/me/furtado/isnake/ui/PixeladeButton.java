package me.furtado.isnake.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import me.furtado.isnake.helper.FontCache;

/**
 * Created by bfurtado on 09/01/16.
 */
public class PixeladeButton extends Button {

    public PixeladeButton(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public PixeladeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public PixeladeButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface typeface = FontCache.getTypeface("Pixelade-Regular.ttf", context);
        setTypeface(typeface);
    }

}
