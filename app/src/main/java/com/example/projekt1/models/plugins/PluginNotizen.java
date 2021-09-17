package com.example.projekt1.models.plugins;

import com.example.projekt1.models.plugins.pluginData.Notiz;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class PluginNotizen extends Plugin<Notiz> {

    public PluginNotizen(String id, String beschreibung, String typ, String chatRef, ArrayList<Notiz> notizen) {
        super(id, beschreibung, chatRef, typ, notizen);
    }
    public PluginNotizen(){
        super();
    }

    @Override
    public void doPluginStuff() {
    }

    public void addNotiz(Notiz n){ this.pluginData.add(n); }

    public void updateNotizText(String id, String text){
        this.pluginData = this.pluginData.stream().map(val -> {
            if(id.equals(val.getId())) val.setInhalt(text);
            return val;
        }).collect(Collectors.toCollection(ArrayList::new));
    }

    public void setNotizen(ArrayList<Notiz> notizen){
        this.pluginData = notizen;
    }
}
