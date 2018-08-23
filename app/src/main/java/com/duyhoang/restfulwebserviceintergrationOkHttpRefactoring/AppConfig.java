package com.duyhoang.restfulwebserviceintergrationOkHttpRefactoring;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.duyhoang.restfulwebserviceintergrationOkHttpRefactoring.network.Util;
import com.duyhoang.restfulwebserviceintergrationOkHttpRefactoring.restbean.Author;
import com.google.gson.GsonBuilder;

/**
 * Created by rogerh on 7/15/2018.
 */

public class AppConfig extends Application {

    private static final String SERVER_ENDPOINT_KEY = "server_endpoint";
    private static final String SAVED_TOKEN_ID = "saved_token";
    private static final String SAVED_USERNAME = "saved_username";
    private static final String SAVED_PASSWORD = "saved_password";
    private static final String REGISTERED_AUTHOR = "user";



    static SharedPreferences sharedPreferences;
    static SharedPreferences.Editor editor;
    public static API_ENDPOINTS seletedEndPoint;
    private boolean isEmulator;

    static Context context;



    public enum API_ENDPOINTS {REMOTE, LOCAL}

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = getSharedPreferences("myapppreference.xml", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        isEmulator = Util.isEmulator();
        context = getApplicationContext();
    }



    public static void saveUsername(String username){
        editor.putString(SAVED_USERNAME, username).commit();
    }

    public static String getSavedUsername(){
        return sharedPreferences.getString(SAVED_USERNAME, null);
    }

    public static void savePassword(String password){
        editor.putString(SAVED_PASSWORD, password).commit();
    }

    public static String getSavedPassword(){
        return sharedPreferences.getString(SAVED_PASSWORD, null);
    }

    public static void setServerEndPointPreference(boolean b) {
        editor.putBoolean(SERVER_ENDPOINT_KEY, b).commit();
    }

    public static boolean getServerEndPointPreference(){
        // seting up the second parameter to be true, because the defautl mode is remote.
        return sharedPreferences.getBoolean(SERVER_ENDPOINT_KEY, true);
    }

    public static Context getContext(){
        return context;
    }


    public static void saveSessionTokenID(String token) {
        editor.putString(SAVED_TOKEN_ID, token).commit();
    }


    public static String getSavedSessionTokenID(){
        return sharedPreferences.getString(SAVED_TOKEN_ID, null);
    }

    public static void saveRegisteredSuccessfulAuthor(String jsonString){
        editor.putString(REGISTERED_AUTHOR, jsonString).commit();
    }

    public static Author getRegisteredSuccessfulAuthor(){
        String json = sharedPreferences.getString(REGISTERED_AUTHOR, null);
        return new GsonBuilder().create().fromJson(json, Author.class);
    }


    public static void clearSharedPreference(){
        editor.clear().commit();
    }



}
