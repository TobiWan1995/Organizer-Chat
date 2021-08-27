package com.example.projekt1.models.plugins;

import java.util.ArrayList;

public abstract class Plugin <T> {
    String Id;
    String typ;
    String beschreibung;
    String chatRef;
    ArrayList<T> pluginData;

    public Plugin(){};

    public Plugin(String typ, String bescheibung, String chatRef){
        this.typ = typ;
        this.beschreibung = bescheibung;
        this.chatRef = chatRef;
    }

    public Plugin(String typ, String bescheibung, String chatRef, ArrayList<T> pluginData){
        this.typ = typ;
        this.beschreibung = bescheibung;
        this.chatRef = chatRef;
        this.pluginData = pluginData;
    }

    public abstract void doPluginStuff();

    public String getTyp(){
        return this.typ;
    }

    public String getBeschreibung(){
        return this.beschreibung;
    }

    public String getChatRef(){
        return this.chatRef;
    }

    public ArrayList<T> getPluginData(){
        return this.pluginData;
    }

}