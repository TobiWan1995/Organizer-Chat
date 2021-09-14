package com.example.projekt1.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.ArraySet;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Chat implements Parcelable {
    private String id;
    private String titel = "default";
    private ArrayList<String> userList = new ArrayList<>();

    public Chat(){}

    public Chat(String id, String titel) {
        this.id = id;
        this.titel = titel;
    }

    public Chat(String id, String titel, ArrayList<String> uList) {
        this.id = id;
        this.titel = titel;
        this.userList = uList;
    }

    // Parcelable.Creator
    protected Chat(Parcel in) {
        id = in.readString();
        titel = in.readString();
    }

    public static final Creator<Chat> CREATOR = new Creator<Chat>() {
        @Override
        public Chat createFromParcel(Parcel in) {
            return new Chat(in);
        }

        @Override
        public Chat[] newArray(int size) {
            return new Chat[size];
        }
    };

    public String getId(){return this.id;}
    
    public String getTitel(){return this.titel;}

    public ArrayList<String> getUsers(){return this.userList;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(titel);
    }

    public void addUsers(ArraySet<String> users) {
        users.addAll(this.userList);
        this.userList.clear();
        this.userList.addAll(users);

    }

    public void addUsers(ArrayList<String> users) {
        for(String user: this.userList){
            users = (ArrayList<String>) users.stream().filter(u -> !u.equals(user)).collect(Collectors.toList());
        }
        this.userList.addAll(users);
    }

    public void removeUser(String username){
        List<String> templist =  this.userList.stream().filter(value -> !value.equals(username)).collect(Collectors.toList());
        this.userList = new ArrayList<String>(templist);
    }
}
