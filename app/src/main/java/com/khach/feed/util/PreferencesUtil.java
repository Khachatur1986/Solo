package com.khach.feed.util;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public final class PreferencesUtil {

    public static void saveIntData(Context context, final String MY_PREFS_NAME, String key, int data) {
        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putInt(key, data);
        editor.apply();
    }

    public static int getIntData(Context context, final String MY_PREFS_NAME, String key) {
        SharedPreferences preferences = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        return preferences.getInt(key, -1);
    }

    public static void saveStringData(Context context, final String MY_PREFS_NAME, String key, String data) {
        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString(key, data);
        editor.apply();
    }

    public static String getStringData(Context context, final String MY_PREFS_NAME, String key) {
        SharedPreferences preferences = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        return preferences.getString(key, null);
    }
}
