package com.example.projekt1.models.plugins;
import android.app.Activity;
import android.app.Application;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projekt1.R;
import com.example.projekt1.activities.chat.ChatActivity;
import com.example.projekt1.models.plugins.pluginData.Poll;
import com.google.firebase.database.collection.LLRBNode;
  import com.example.projekt1.models.plugins.Plugin;
import com.example.projekt1.models.plugins.pluginData.Poll;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.stream.Collectors;


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

    public void updatePoll (Poll p){
        this.pluginData = this.pluginData.stream().map(val -> {
            if(val.equals(p)) val = p;
            return val;
        }).collect(Collectors.toCollection(ArrayList::new));
    }

    public void updatePollText(String id, String text){
        this.pluginData = this.pluginData.stream().map(val -> {
            if(id == val.getId()) val.setTitle(text);
            return val;
        }).collect(Collectors.toCollection(ArrayList::new));
    };

    public void addPoll(Poll p){ this.pluginData.add(p);}

    public Poll getPollById(String id){
        ArrayList<Poll> temp = this.pluginData.stream().filter(val -> val.getId().equals(id)).collect(Collectors.toCollection(ArrayList::new));
        return temp.get(0) != null ? temp.get(0) : null;
    }

    public void setPolls(ArrayList<Poll> newPolls){
        this.pluginData = newPolls;
    }
}
