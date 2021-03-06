package com.example.tapos.fullapplicationdevelopment.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.tapos.fullapplicationdevelopment.model.CreatorInfo;
import com.example.tapos.fullapplicationdevelopment.model.LoginInformation;
import com.example.tapos.fullapplicationdevelopment.model.User;

/**
 * Created by tapos on 9/26/17.
 */

public class SharedPrefManager {
    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private static final String SHARED_PREF_NAME = "simplifiedcodingsharedprefretrofit";

    private static final String KEY_USER_ID = "keyuserid";
    private static final String KEY_USER_NAME = "keyusername";
    private static final String KEY_USER_EMAIL = "keyuseremail";
    private static final String KEY_USER_PROFILE_PIC = "keyuseremail";
    private static final String KEY_USER_TOKEN_TYPE = "keyusertokentype";
    private static final String KEY_USER_ACCESS_TOKEN = "keyuseraccesstoken";
    private static final String KEY_USER_REFRESH_TOKEN = "keyuserrefreshtoken";
    private static final String KEY_USER_TOKEN_EXPIRE = "keyusertokenexpire";

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public boolean userLoginDataUpdate(LoginInformation loginData) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_USER_TOKEN_TYPE, loginData.getToken_type());
        editor.putString(KEY_USER_ACCESS_TOKEN, loginData.getAccess_token());
        editor.putString(KEY_USER_REFRESH_TOKEN, loginData.getRefresh_token());
        editor.putInt(KEY_USER_TOKEN_EXPIRE, loginData.getExpires_in());
        editor.apply();
        return true;
    }

    public boolean userOwnDataUpdate(CreatorInfo user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_USER_ID, user.getId());
        editor.putString(KEY_USER_NAME, user.getName());
        editor.putString(KEY_USER_EMAIL, user.getEmail());
        editor.putString(KEY_USER_PROFILE_PIC, user.getCoverPhoto());
        editor.apply();
        return true;
    }


    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        if (sharedPreferences.getString(KEY_USER_ACCESS_TOKEN, null) != null)
            return true;
        return false;
    }

    public String getAuthToken(){

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String info = sharedPreferences.getString(KEY_USER_TOKEN_TYPE, null)
                +" "+ sharedPreferences.getString(KEY_USER_ACCESS_TOKEN, null);
        System.out.println(info);
        return info;
    }

    public boolean isGetUserData() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        if (sharedPreferences.getString(KEY_USER_EMAIL, null) != null)
            return true;
        return false;
    }

    public User getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        return new User(
                sharedPreferences.getInt(KEY_USER_ID, 0),
                sharedPreferences.getString(KEY_USER_NAME, null),
                sharedPreferences.getString(KEY_USER_EMAIL, null),

                sharedPreferences.getString(KEY_USER_PROFILE_PIC, null)
        );
    }

    public boolean logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;
    }
}
