package com.example.projekt1.models;

import java.util.ArrayList;

public class PollOption {

    String id;
    String optionTitle;
    ArrayList<String> userRef;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOptionTitle() {
        return optionTitle;
    }

    public void setOptionTitle(String optionTitle) {
        this.optionTitle = optionTitle;
    }

    public ArrayList<String> getUserRef() {
        return userRef;
    }

    public void setUserRef(ArrayList<String> userRef) {
        this.userRef = userRef;
    }
}
