package com.example.ace.vary.modelsAndAdapters;

import java.io.Serializable;

public class NewPetModel implements Serializable {
    public String imageID,name,pid,title,uid;

    public NewPetModel() {
    }

    public NewPetModel(String imageID, String name, String pid, String title, String uid) {
        this.imageID = imageID;
        this.name = name;
        this.pid = pid;
        this.title = title;
        this.uid = uid;
    }
}
