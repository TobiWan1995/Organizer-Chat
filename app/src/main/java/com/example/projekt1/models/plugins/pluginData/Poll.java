package com.example.projekt1.models.plugins.pluginData;

<<<<<<< HEAD
import java.util.ArrayList;

public class Poll {

    private String id;
    //private boolean chooseMultiple;
    private String title;
    private ArrayList<String> userRef;


    public Poll(String id, ArrayList<String> userRef, String title) {
        this.id = id;
        this.title = title;
        this.userRef = userRef;


    }

    public ArrayList<String> getUserRef() {
        return userRef;
    }

    public void setUserRef(ArrayList<String> userRef) {
        this.userRef = userRef;
    }

    public String getId() {return id;}

    public void setId(String id) {this.id = id;}

    public String getTitle() {return title;}

    public void setTitle(String title) {this.title = title;}




}


=======
public class Poll {
}
>>>>>>> origin/master
