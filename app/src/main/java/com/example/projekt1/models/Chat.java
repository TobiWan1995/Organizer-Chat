package com.example.projekt1.models;

import java.util.List;

public class Chat {
    String titel = "default";
    List<User> userList;
    List<Message> messagesList;

    Chat(){}
    Chat(String titel, List<User> uList, List<Message> mList) {
        this.titel = titel;
        this.userList = uList;
        this.messagesList = mList;
    }
}
