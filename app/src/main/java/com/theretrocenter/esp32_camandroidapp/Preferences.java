package com.theretrocenter.esp32_camandroidapp;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
    private static Preferences yourPreference;
    private SharedPreferences sharedPreferences;

    public static Preferences getInstance(Context context) {
        if (yourPreference == null) {
            yourPreference = new Preferences(context);
        }
        return yourPreference;
    }

    private Preferences(Context context) {
        sharedPreferences = context.getSharedPreferences("YourCustomNamedPreference", Context.MODE_PRIVATE);
    }

    public void saveData(String key,String value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor .putString(key, value);
        prefsEditor.commit();
    }

    public String getData(String key) {
        if (sharedPreferences!= null) {
            return sharedPreferences.getString(key, "");
        }
        return "";
    }
}
