package com.example.ace.vary;

public class petetionModelDisplay {
    public String pass;
    public String email,decesionMaker,name,imageID,signatures,solution,pid,date;


    public petetionModelDisplay() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public petetionModelDisplay(String pass, String email, String decedionMaker, String name, String imageID, String signatures, String solution, String pid,String date) {
        this.pass = pass;
        this.email = email;
        this.date=date;
        this.decesionMaker = decedionMaker;
        this.name = name;
        this.imageID = imageID;
        this.signatures = signatures;
        this.solution = solution;
        this.pid = pid;
    }
}
