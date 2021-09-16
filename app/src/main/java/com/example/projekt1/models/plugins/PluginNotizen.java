package com.example.projekt1.models.plugins;

import com.example.projekt1.models.plugins.pluginData.Notiz;

import java.util.ArrayList;

public class PluginNotizen extends Plugin<Notiz> {
    public PluginNotizen(String id, String beschreibung, String chatRef, ArrayList<Notiz> notizen) {
        super(id, "pluginNotizen", beschreibung, chatRef, notizen);
    }

    public PluginNotizen(String beschreibung, String chatRef) {
        super("pluginNotizen", beschreibung, chatRef);
    }

    @Override
    public void doPluginStuff() {

    }
}
