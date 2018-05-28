package com.example.caio.clothingstore.Main.Helper;


import android.content.Context;
import android.content.SharedPreferences;


public class Preferences {

    private Context context;
    private SharedPreferences preferences;
    private final int MODE = 0;
    private final String FILE_NAME = "Settings";
    private SharedPreferences.Editor editor;


    public Preferences( Context parameterContext){

        context = parameterContext;
        preferences = context.getSharedPreferences(FILE_NAME, MODE );
        editor = preferences.edit();
    }


    public void saveData( String key, String value ){

        editor.putString(key, value);
        editor.commit();
    }

    public String getIdentifier(String key){

        return preferences.getString(key, "NOT FOUND");
    }
}

