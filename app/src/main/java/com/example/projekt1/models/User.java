package com.example.projekt1.models;

import android.util.ArraySet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class User {
    private String id;
    private String fullname, userName;
    private String eMail, password, phoneNumber;
    private String gender, birth;
    private ArrayList<String> userList = new ArrayList<>();

    public User(){}

    public User(String id, String fullName, String userName, String eMail, String password, String gender, String birth, String phoneNumber){
        this.id = id;
        this.fullname = fullName;
        this.userName = userName;
        this.eMail = eMail;
        this.password = password;
        this.gender = gender;
        this.birth = birth;
        this.phoneNumber = phoneNumber;
    }

    public User(String id, String fullName, String userName, String eMail, String password, String gender, String birth, String phoneNumber, ArraySet<String> users){
        this.id = id;
        this.fullname = fullName;
        this.userName = userName;
        this.eMail = eMail;
        this.password = password;
        this.gender = gender;
        this.birth = birth;
        this.phoneNumber = phoneNumber;
        userList.addAll(users);
    }


    public String getId() { return this.id; }

    public String getFullname() { return this.fullname; }

    public String getUserName() { return this.userName; }

    public String geteMail() { return this.eMail; }

    public String getPassword() { return this.password;}

    public String getGender(){ return this.gender;}

    public String getBirth(){ return this.birth;}

    public String getPhoneNumber(){ return this.phoneNumber;}

    public ArrayList<String> getUsers(){ return this.userList;}
    
    public void addUserCollection(Collection<String> users){
        this.userList.addAll(users);
    }

    public void addUser(String user){
        this.userList.add(user);
    }


}
