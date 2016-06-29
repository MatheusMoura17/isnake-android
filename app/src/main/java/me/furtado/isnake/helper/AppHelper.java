package me.furtado.isnake.helper;

import android.graphics.Rect;
import android.view.View;

/**
 * Created by bfurtado on 10/01/16.
 */
public class AppHelper {

    public static boolean viewsIntersect(View view01, View view02) {
        Rect rect01 = new Rect();
        //view01.getDrawingRect(rect01);
        view01.getHitRect(rect01);

        Rect rect02 = new Rect();
        //view02.getDrawingRect(rect02);
        view02.getHitRect(rect02);

        return Rect.intersects(rect01, rect02);
    }

    public static int randomNumberWithRange(int min, int max) {
        int range = (max - min) + 1;
        return (int)(Math.random() * range) + min;
    }

}
