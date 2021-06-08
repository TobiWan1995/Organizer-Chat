package com.example.projekt1.models;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String id;
    private String fullname, userName;
    private String eMail, password;
    private String gender, birth;

    public User(){}

    public User(String id, String fullName, String userName, String eMail, String password, String gender, String birth){
        this.id = id;
        this.fullname = fullName;
        this.userName = userName;
        this.eMail = eMail;
        this.password = password;
        this.gender = gender;
        this.birth = birth;
    }

    public String getId() { return this.id; }

    public String getFullname() { return this.fullname; }

    public String getUserName() { return this.userName; }

    public String geteMail() { return this.eMail; }

    public String getPassword() { return this.password;}

    public String getGender(){ return this.gender;}

    public String getBirth(){ return this.birth;}
}
