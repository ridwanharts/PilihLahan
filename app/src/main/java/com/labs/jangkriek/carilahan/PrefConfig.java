package com.labs.jangkriek.carilahan;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class PrefConfig {

    static final String KEY_USER_TEREGISTER ="user";
    static final String KEY_USERNAME_SEDANG_LOGIN = "Username_logged_in";
    static final String KEY_STATUS_SEDANG_LOGIN = "Status_logged_in";
    static final String KEY_LOGIN_TYPE = "login_type";



    /** Pendlakarasian Shared Preferences yang berdasarkan paramater context */
    private static SharedPreferences getSharedPreference(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setTypeLogin(Context context, String type){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(KEY_LOGIN_TYPE, type);
        editor.apply();
    }

    /** Mengembalikan nilai dari key KEY_USER_TEREGISTER berupa String */
    public static String getTypeLogin(Context context){
        return getSharedPreference(context).getString(KEY_LOGIN_TYPE,"");
    }

    /** Deklarasi Edit Preferences dan mengubah data
     *  yang memiliki key isi KEY_USER_TEREGISTER dengan parameter username */
    public static void setUsernameLogin(Context context, String username){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(KEY_USER_TEREGISTER, username);
        editor.apply();
    }

    /** Mengembalikan nilai dari key KEY_USER_TEREGISTER berupa String */
    public static String getUsernameLogin(Context context){
        return getSharedPreference(context).getString(KEY_USER_TEREGISTER,"");
    }

    /** Deklarasi Edit Preferences dan mengubah data
     *  yang memiliki key KEY_USERNAME_SEDANG_LOGIN dengan parameter username */
    public static void setLoggedInUser(Context context, int id){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putInt(KEY_USERNAME_SEDANG_LOGIN, id);
        editor.apply();
    }

    /** Mengembalikan nilai dari key KEY_USERNAME_SEDANG_LOGIN berupa String */
    public static int getLoggedInUser(Context context){
        return getSharedPreference(context).getInt(KEY_USERNAME_SEDANG_LOGIN,4);
    }

    /** Deklarasi Edit Preferences dan mengubah data
     *  yang memiliki key KEY_STATUS_SEDANG_LOGIN dengan parameter status */
    public static void setLoggedInStatus(Context context, boolean status){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putBoolean(KEY_STATUS_SEDANG_LOGIN,status);
        editor.apply();
    }

    /** Mengembalikan nilai dari key KEY_STATUS_SEDANG_LOGIN berupa boolean */
    public static boolean getLoggedInStatus(Context context){
        return getSharedPreference(context).getBoolean(KEY_STATUS_SEDANG_LOGIN,false);
    }

    /** Deklarasi Edit Preferences dan menghapus data, sehingga menjadikannya bernilai default
     *  khusus data yang memiliki key KEY_USERNAME_SEDANG_LOGIN dan KEY_STATUS_SEDANG_LOGIN */
    public static void clearLoggedInUser (Context context){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.remove(KEY_USERNAME_SEDANG_LOGIN);
        editor.remove(KEY_STATUS_SEDANG_LOGIN);
        editor.remove(KEY_LOGIN_TYPE);
        editor.remove(KEY_USER_TEREGISTER);
        editor.apply();
    }
}
