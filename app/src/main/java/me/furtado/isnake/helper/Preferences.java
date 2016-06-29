package me.furtado.isnake.helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by bfurtado on 09/01/16.
 */
public class Preferences {

    private static final String BEST_SCORE_KEY = "me.furtado.isnake.v1.best_score";

    public static void setBestScore(int bestScore, Context context) {
        SharedPreferences preferences = getSharedPreferences(BEST_SCORE_KEY, context);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(BEST_SCORE_KEY, bestScore);
        editor.commit();
    }

    public static int getBestScore(Context context) {
        SharedPreferences preferences = getSharedPreferences(BEST_SCORE_KEY, context);

        int bestScore = preferences.getInt(BEST_SCORE_KEY, 0);

        return bestScore;
    }

    private static SharedPreferences getSharedPreferences(String key, Context context) {
        String packageName = context.getPackageName();
        String preferencesKey = String.format("%s.%s", packageName, key);

        SharedPreferences preferences = context.getSharedPreferences(preferencesKey, context.MODE_PRIVATE);

        return preferences;
    }

}
