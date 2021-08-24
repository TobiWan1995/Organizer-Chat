package com.example.projekt1.models.plugins.pluginData;

public class Notiz {
    private String id;
    private String inhalt;
    private String datum;
    private boolean abgeschlossen;

    public Notiz(String id, String inhalt){
        this.id = id;
        this.inhalt = inhalt;
        this.datum = String.valueOf(System.currentTimeMillis());
        this.abgeschlossen = false;
    }

    public String getId() {
        return id;
    }

    public String getInhalt() {
        return inhalt;
    }

    public String getDatum() {
        return datum;
    }

    public boolean isAbgeschlossen() {
        return abgeschlossen;
    }

    public void setAbgeschlossen(boolean abgeschlossen) {
        this.abgeschlossen = abgeschlossen;
    }
}
