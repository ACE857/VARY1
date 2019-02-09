package com.example.ace.vary.modelsAndAdapters;

import java.io.Serializable;

public class SignatureModel implements Serializable {
    public String pid,title,uid;

    public SignatureModel() {
    }

    public SignatureModel(String pid, String title, String uid) {
        this.pid = pid;
        this.title = title;
        this.uid = uid;
    }

    
}
