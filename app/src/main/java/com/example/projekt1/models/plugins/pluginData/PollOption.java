package com.example.projekt1.models.plugins.pluginData;

import java.util.ArrayList;

public class PollOption {
    private String id;
    private String optionTitle;
    private ArrayList<String> userRef;

    public PollOption(){ };

    public PollOption(String id, ArrayList<String> userRef){
        this.userRef = userRef;
        this.id = id;
    };

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

    public boolean containsUser(String username){
        return this.userRef.contains(username);
    }

    public void removeUserFromSub(String username){
        this.userRef.remove(username);
    }

    public void addUserToSub(String username){
        this.userRef.add(username);
    }
}
