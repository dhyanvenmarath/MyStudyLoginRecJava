package com.dhyan.mystudyloginrecjava.db;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.dhyan.mystudyloginrecjava.models.MainResponseValues;

public class SharedPreferenceManager {

    private static final String Shared_PName = "my shared pre";
    @SuppressLint("StaticFieldLeak")
    private static SharedPreferenceManager minstencepre;
    private Context mCtx;

    private SharedPreferenceManager(Context mCtx) {
        this.mCtx = mCtx;
    }

    public static synchronized SharedPreferenceManager getInstence(Context mCtx) {
        if (minstencepre == null) {
            minstencepre = new SharedPreferenceManager(mCtx);
        }
        return minstencepre;
    }

    public void saveUser(MainResponseValues rv) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Shared_PName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", rv.getEmail());
        editor.putString("password", rv.getPassword());
        editor.apply();
    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Shared_PName, Context.MODE_PRIVATE);
        return sharedPreferences.getString("email", null) != null;
    }

    public void clear() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(Shared_PName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
