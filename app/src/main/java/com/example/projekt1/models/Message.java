package com.example.projekt1.models;

import java.sql.Date;

public class Message {
    private long id;
    private Date timeStamp;
    private String content;
    private User user;

    public Message(long id, String content, User user){
        this.id = id;
        this.content = content;
        this.timeStamp = new java.sql.Date(System.currentTimeMillis());
        this.user = user;
    }

    public long getId(){ return this.id;}
    public Date getTimeStamp(){ return this.timeStamp;}
    public String getContent(){ return this.content;}
}
