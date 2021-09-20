package com.example.projekt1.models.plugins;

import com.example.projekt1.models.plugins.pluginData.Poll;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class PluginPoll extends Plugin<Poll> {

    public PluginPoll (String id, String beschreibung, String chatRef, String typ) {
        super(id, beschreibung, chatRef, "pluginPoll");
    }

    public PluginPoll(String id, String beschreibung, String typ, String chatRef, ArrayList<Poll> polls){
        super(id, beschreibung, typ, chatRef, polls);
    }

    public PluginPoll () {
        super();
    }

    @Override
    public void doPluginStuff() {

    }

    public void addPoll(Poll p){ this.pluginData.add(p);}

    public void setPolls(ArrayList<Poll> newPolls){
        this.pluginData = newPolls;
    }
}
