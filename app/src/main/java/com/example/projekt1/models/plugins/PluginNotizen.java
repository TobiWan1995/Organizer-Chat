package com.example.projekt1.models.plugins;

import com.example.projekt1.models.plugins.pluginData.Notiz;

import java.util.ArrayList;

public class PluginNotizen extends Plugin<Notiz> {
    public PluginNotizen(String typ, String bescheibung, String chatRef) {
        super(typ, bescheibung, chatRef);
    }

    @Override
    public void doPluginStuff() {

    }
}
