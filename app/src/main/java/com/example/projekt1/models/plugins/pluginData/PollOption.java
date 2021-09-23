package com.example.projekt1.models.plugins.pluginData;


import java.util.ArrayList;

public class PollOption {

    private String id;
    // private boolean chooseMultiple;
    private String title;
    private String thema;
    private String pollRef; // ist doppelt - liste in poll reicht aus
    private ArrayList<String> users; // counter implicit




    public PollOption(String id, ArrayList<String> userRef, String title) {
        this.id = id;
        this.title = title;
        this.users = users;
    }

    public ArrayList<String> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<String> users) {
        this.users = users;
    }

    public String getId() {return id;}

    public void setId(String id) {this.id = id;}

    public String getTitle() {return title;}

    public void setTitle(String title) {this.title = title;}




}
