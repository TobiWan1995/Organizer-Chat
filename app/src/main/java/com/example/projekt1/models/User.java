package com.example.projekt1.models;

import java.util.ArrayList;
import java.util.List;

public class User {
    long id;
    String firstName, lastName;
    String nickName;

    public User(long id, String firstName, String lastName, String nickName){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nickName = nickName;
    }

    public long getId() { return this.id; }
}
