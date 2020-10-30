package com.example.trendz;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManagement {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String SHARED_PREF_NAME = "session";
    String SESSION_KEY = "session_";

    public SessionManagement(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveSession(User user){
        String id = user.getId();
        String FullName = user.getFullname();
        String Sponsor = user.getSponsor();
        String Email = user.getEmail();
        String UserName = user.getUsername();
        String Mobile = user.getMobile();
        String isJoin = user.getIsJoin();

        editor.putString(SESSION_KEY + "id", id);
        editor.putString(SESSION_KEY + "FullName", FullName);
        editor.putString(SESSION_KEY + "Sponsor", Sponsor);
        editor.putString(SESSION_KEY + "email", Email);
        editor.putString(SESSION_KEY + "username", UserName);
        editor.putString(SESSION_KEY + "Mobile", Mobile);
        editor.putString(SESSION_KEY + "isJoin", isJoin);
        editor.commit();
    }

    public String getSession(String Field){
        return sharedPreferences.getString(SESSION_KEY + Field, "");
    }

    public String getSessionEmail(){
        return sharedPreferences.getString(SESSION_KEY + "email", "");
    }

    public String getSessionUserId() {
        return sharedPreferences.getString(SESSION_KEY + "id", "");
    }

    public void ClearSession() {
        editor.clear();
        editor.commit();
    }
}
