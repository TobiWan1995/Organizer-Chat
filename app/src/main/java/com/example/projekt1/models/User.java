package com.example.projekt1.models;

import java.util.ArrayList;
import java.util.List;

public class User {
    long id;
    String firstName, lastName;
    String nickName;
    private List<Message> messagesList;

    public User(long id, String firstName, String lastName, String nickName){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nickName = nickName;
        this.messagesList = new ArrayList<Message>();
    }

    public List<Message> getMessages(){
        return this.messagesList;
    }

    public void postMessage(Message message){
        this.messagesList.add(message);
    }
}
