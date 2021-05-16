package com.example.projekt1.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Chat implements Parcelable {
    private long id;
    private String titel = "default";
    private ArrayList<User> userList = new ArrayList<User>();
    private ArrayList<Message> messageList = new ArrayList<Message>();

    public Chat(){}

    public Chat(long id, String titel) {
        this.id = id;
        this.titel = titel;
    }

    public Chat(long id, String titel, ArrayList<User> uList, ArrayList<Message> mList) {
        this.id = id;
        this.titel = titel;
        this.userList = uList;
        this.messageList = mList;
    }

    // Parcelable.Creator
    protected Chat(Parcel in) {
        id = in.readLong();
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

    public long getId(){return this.id;}
    
    public String getTitel(){return this.titel;}

    public ArrayList<User> getUsers(){return this.userList;}
    
    public ArrayList<Message> getMessages(){return this.messageList;}

    public void sendMessage(Message message){
        this.messageList.add(message);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(titel);
    }

    public void addUser(User user) {
        this.userList.add(user);
    }
}
