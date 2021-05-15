package com.example.projekt1.models;

import java.sql.Date;

public class Message {
    private long id;
    private long chatIdent;
    private Date timeStamp;
    private String content;

    public Message(long id, String content, long chatIdent){
        this.id = id;
        this.chatIdent = chatIdent;
        this.content = content;
        this.timeStamp = new java.sql.Date(System.currentTimeMillis());
    }

    public long getId(){ return this.id;}
    public Date getTimeStamp(){ return this.timeStamp;}
    public String getContent(){ return this.content;}
}
