package tn.chantier.chantiertn.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import tn.chantier.chantiertn.models.City;

import static android.content.Context.MODE_PRIVATE;

public class UtilsSharefPreferences {



    public static final String MY_PREFS_NAME = "MyPrefsFile";
    public static void saveSplashScreenState(Context context, boolean value){
        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putBoolean("firstInstallation", value);
        editor.commit();
    }



    public static boolean getSplashScreenState(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        boolean restoredText = prefs.getBoolean("firstInstallation", true);
        return restoredText;
    }
}

