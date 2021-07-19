package com.theretrocenter.esp32_camandroidapp.utilities;

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

    // Constructor
    private Preferences(Context context) {
        // App key to manage data
        sharedPreferences = context.getSharedPreferences("RemoteWIFICar", Context.MODE_PRIVATE);
    }

    // Save method
    public void saveData(String key,String value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor .putString(key, value);
        prefsEditor.commit();
    }

    // Read method
    public String getData(String key) {
        if (sharedPreferences!= null) {
            return sharedPreferences.getString(key, "");
        }
        return "";
    }
}
