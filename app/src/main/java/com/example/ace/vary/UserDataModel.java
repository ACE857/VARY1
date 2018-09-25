package com.example.ace.vary;

import java.io.Serializable;

/**
 * Created by ace on 9/14/18.
 */

public class UserDataModel implements Serializable {

    String email,level,name,pass,userid;

    public UserDataModel(String email, String level, String name, String pass) {
        this.email = email;
        this.level = level;
        this.name = name;
        this.pass = pass;
    }

    public UserDataModel() {
    }

    public UserDataModel(String email, String level, String name, String pass, String userid) {
        this.email = email;
        this.level = level;
        this.name = name;
        this.pass = pass;
        this.userid = userid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
