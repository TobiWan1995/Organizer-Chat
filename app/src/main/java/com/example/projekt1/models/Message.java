package com.example.projekt1.models;

import java.sql.Date;

public class Message {
    private long id;
    private String timeStamp;
    private String content;
    private User user;

    public Message() {}

    public Message(long id, String content, User user){
        this.id = id;
        this.content = content;
        this.timeStamp = String.valueOf(System.currentTimeMillis());
        this.user = user;
    }

    public long getId(){ return this.id;}
    public String getTimeStamp(){ return this.timeStamp;}
    public String getContent(){ return this.content;}
    public User getUser(){ return this.user;}

}
