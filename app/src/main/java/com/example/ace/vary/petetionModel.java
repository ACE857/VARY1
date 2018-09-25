package com.example.ace.vary;

import java.io.Serializable;

/**
 * Created by ace on 9/18/18.
 */

public class petetionModel implements Serializable {
    public String uid,pid,title,decesionMaker,solution,imageID,name,signatures,date;

    public petetionModel() {
    }

    public petetionModel(String uid, String pid, String title, String decesionMaker, String solution, String imageID, String name, String signatures,String date) {
        this.date=date;
        this.uid = uid;
        this.pid = pid;
        this.title = title;
        this.decesionMaker = decesionMaker;
        this.solution = solution;
        this.imageID = imageID;
        this.name = name;
        this.signatures = signatures;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDecesionMaker() {
        return decesionMaker;
    }

    public void setDecesionMaker(String decesionMaker) {
        this.decesionMaker = decesionMaker;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getImageID() {
        return imageID;
    }

    public void setImageID(String imageID) {
        this.imageID = imageID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSignatures() {
        return signatures;
    }

    public void setSignatures(String signatures) {
        this.signatures = signatures;
    }
}
