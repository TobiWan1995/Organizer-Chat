package com.example.projekt1.models.plugins;

import com.example.projekt1.models.plugins.pluginData.Notiz;

import java.util.ArrayList;

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
}
