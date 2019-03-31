package com.example.munde.unskript.activity.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by munde on 29/03/2019.
 */

public class SharedPreferenceManager {
    private static SharedPreferenceManager ourInstance = new SharedPreferenceManager();
    private String defaultString = "";

    public static SharedPreferenceManager getInstance() {
        return ourInstance;
    }

    public SharedPreferenceManager() {
    }


    //USER ID
//    public static void setUserId(Context c, String userId)
//    {
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("UserId", userId);
//        editor.commit();
//    }
//
//    public static String getUserId(Context c)
//    {
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
//        return sharedPreferences.getString("UserId", "");
//    }


}
