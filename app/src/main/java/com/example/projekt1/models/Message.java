package com.example.projekt1.models;

import java.sql.Date;

public class Message {
    private String id;
    private String timeStamp;
    private String content;
    private String userId;
    private String chatId;

    public Message() {}

    public Message(String id, String content, String userId, String chatId){
        this.id = id;
        this.content = content;
        this.timeStamp = String.valueOf(System.currentTimeMillis());
        this.userId = userId;
        this.chatId = chatId;
    }

    public String getId(){ return this.id;}
    public String getTimeStamp(){ return this.timeStamp;}
    public String getContent(){ return this.content;}
    public String getUserId(){ return this.userId;}
    public String getChatId(){ return this.chatId;}

}
