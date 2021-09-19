package com.example.projekt1.models.plugins.pluginData;


import com.example.projekt1.models.PollOption;

import java.util.ArrayList;

public class Poll {

    private String id;
    private boolean multiOption;
    private String title;
    private ArrayList<String> userRef;
    private ArrayList<PollOption> pollOptions;


    public Poll(String id, boolean multiOption,  String title, ArrayList<String> userRef, ArrayList<PollOption> pollOptions) {
        this.id = id;
        this.multiOption = multiOption;
        this.title = title;
        this.userRef = userRef;
        this.pollOptions = pollOptions;


    }

    public ArrayList<String> getUserRef() {
        return userRef;
    }

    public void setUserRef(ArrayList<String> userRef) {
        this.userRef = userRef;
    }

    public String getId() {return id;}

    public void setId(String id) {this.id = id;}

    public String getTitle() {return title;}

    public void setTitle(String title) {this.title = title;}

    public boolean isMultiOption() {
        return multiOption;
    }

    public void setMultiOption(boolean multiOption) {
        this.multiOption = multiOption;
    }

    public ArrayList<PollOption> getPollOptions() {
        return pollOptions;
    }

    public void setPollOptions(ArrayList<PollOption> pollOptions) {
        this.pollOptions = pollOptions;
    }
}
