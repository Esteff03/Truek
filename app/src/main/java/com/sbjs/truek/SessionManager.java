package com.sbjs.truek;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "SupabaseSession";
    private static final String KEY_TOKEN = "session_token";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveSessionToken(String token) {
        editor.putString(KEY_TOKEN, token);
        editor.apply();
    }

    public String getSessionToken() {
        return sharedPreferences.getString(KEY_TOKEN, null);
    }

    public void clearSession() {
        editor.remove(KEY_TOKEN);
        editor.apply();
    }
}
