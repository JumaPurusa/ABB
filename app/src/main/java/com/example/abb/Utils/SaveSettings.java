package com.example.abb.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SaveSettings {

    private static final String TAG = SaveSettings.class.getSimpleName();
    private static SharedPreferences sharedPreferences;

    public static String getUserProfile(Context context){

        if(sharedPreferences == null)
            sharedPreferences = context.getSharedPreferences("abb", Context.MODE_PRIVATE);

        String userString = sharedPreferences.getString("user", null);
        return userString;

    }

    public static void userProfile(Context context, String profile){

        if(sharedPreferences == null)
            sharedPreferences = context.getSharedPreferences("abb", Context.MODE_PRIVATE);

        sharedPreferences.edit().putString("user", profile).apply();
    }

}
