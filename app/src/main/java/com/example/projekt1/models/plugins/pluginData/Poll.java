package com.example.projekt1.models.plugins.pluginData;

import com.example.projekt1.models.User;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Poll {
    private String id;
    private boolean multiOption;
    private String title;
    private ArrayList<PollOption> pollOptions;
    private ArrayList<String> subUser;


    public Poll(String id, boolean multiOption,  String title, ArrayList<PollOption> pollOptions, ArrayList<String> subUser) {
        this.id = id;
        this.multiOption = multiOption;
        this.title = title;
        this.pollOptions = pollOptions;
        this.subUser = subUser;
    }

    public Poll () {

    }

    public String getId() {return id;}

    public void setId(String id) {this.id = id;}

    public String getTitle() {return title;}

    public ArrayList<String> getSubUser() {
        return subUser;
    }

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

    public boolean containsSubUser(String username){
        return this.subUser.contains(username);
    }

    public void setSubUser(ArrayList<String> subUser) {
        this.subUser = subUser;
    }

    public void setPollOptions(ArrayList<PollOption> pollOptions) {
        this.pollOptions = pollOptions;
    }

    public void removeUserFromSub(String username){
        this.subUser.remove(username);
    }

    public void addUserToSub(String username){
        this.subUser.add(username);
    }

    public void addPollOption(PollOption n){ this.pollOptions.add(n); }

    public PollOption returnResult(){
        PollOption winner = this.pollOptions.get(0);
        for ( PollOption pollOption : this.pollOptions){
            if (pollOption.getUserRef().size() > winner.getUserRef().size()) winner = pollOption;
        }
        return winner;
    }

    public void updatePollOptionText(String id, String text){
        this.pollOptions= this.pollOptions.stream().map(val -> {
            if(id.equals(val.getId())) val.setOptionTitle(text);
            return val;
        }).collect(Collectors.toCollection(ArrayList::new));
    }

    public void updatePollOption(PollOption pollOption) {
        this.pollOptions= this.pollOptions.stream().map(val -> {
            if(val.equals(pollOption)) val = pollOption;
            return val;
        }).collect(Collectors.toCollection(ArrayList::new));
    }
}
