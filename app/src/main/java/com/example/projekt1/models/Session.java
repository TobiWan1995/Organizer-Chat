package com.example.projekt1.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.ArraySet;

import java.util.Set;

public class Session {

    private SharedPreferences prefs;

    public Session(Context cntx) {
        // TODO Auto-generated constructor stub
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
    }

    public void setUser(User user) {
        prefs.edit().putString("id", user.getId()).apply();
        prefs.edit().putString("username", user.getUserName()).apply();
        prefs.edit().putString("fullname", user.getFullname()).apply();
        prefs.edit().putString("eMail", user.geteMail()).apply();
        prefs.edit().putString("password", user.getPassword()).apply();
        prefs.edit().putString("gender", user.getGender()).apply();
        prefs.edit().putString("birth", user.getBirth()).apply();
        prefs.edit().putStringSet("users", (Set<String>) user.getUsers()).apply();
    }

    public String getId() {
        return prefs.getString("id", "");
    }

    public String getFullname() {
        return prefs.getString("fullname", "");
    }

    public String getUserName() {
        return prefs.getString("username", "");
    }

    public String geteMail() {
        return prefs.getString("eMail", "");
    }

    public String getPassword() {
        return prefs.getString("password", "");
    }

    public String getGender(){
        return prefs.getString("gender", "");
    }

    public String getBirth(){
        return prefs.getString("birth", "");
    }

    public ArraySet<String> getUsers() { return (ArraySet<String>) prefs.getStringSet("users", null ); }
}