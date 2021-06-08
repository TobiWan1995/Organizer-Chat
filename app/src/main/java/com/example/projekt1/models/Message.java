package com.example.projekt1.models;

import java.sql.Date;

public class Message {
    private String id;
    private String timeStamp;
    private String content;
    private String userId;

    public Message() {}

    public Message(String id, String content, String userId){
        this.id = id;
        this.content = content;
        this.timeStamp = String.valueOf(System.currentTimeMillis());
        this.userId = userId;
    }

    public String getId(){ return this.id;}
    public String getTimeStamp(){ return this.timeStamp;}
    public String getContent(){ return this.content;}
    public String getUserId(){ return this.userId;}

}
