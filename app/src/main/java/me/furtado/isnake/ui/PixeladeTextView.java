package me.furtado.isnake.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import me.furtado.isnake.helper.FontCache;

/**
 * Created by bfurtado on 08/01/16.
 */
public class PixeladeTextView extends TextView {

    public PixeladeTextView(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public PixeladeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public PixeladeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface typeface = FontCache.getTypeface("Pixelade-Regular.ttf", context);
        setTypeface(typeface);
    }
}
