package com.example.projekt1.models.plugins;

import java.util.ArrayList;

public abstract class Plugin <T> {
    String id;
    String typ;
    String beschreibung;
    String chatRef;
    ArrayList<T> pluginData;

    public Plugin(){};

    public Plugin(String id, String beschreibung, String chatRef, String typ){
        this.id = id;
        this.typ = typ;
        this.beschreibung = beschreibung;
        this.chatRef = chatRef;
    }

    public Plugin(String id, String beschreibung, String typ, String chatRef, ArrayList<T> pluginData){
        this.id = id;
        this.typ = typ;
        this.beschreibung = beschreibung;
        this.chatRef = chatRef;
        this.pluginData = pluginData;
    }

    public abstract void doPluginStuff();

    public String getId() { return this.id; }

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

    public void setId(String id){ this.id = id; }

}