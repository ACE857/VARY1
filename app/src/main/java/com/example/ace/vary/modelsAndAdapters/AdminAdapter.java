package com.example.ace.vary.modelsAndAdapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ace.vary.R;

public class AdminAdapter extends RecyclerView.ViewHolder {
    public View mview;
    public Button approve,reject;
    public AdminAdapter(View itemView) {
        super(itemView);
        mview = itemView;
        approve = mview.findViewById(R.id.adminApprove);
        reject = mview.findViewById(R.id.adminReject);


    }
    public void setName(String name){
        TextView recName = mview.findViewById(R.id.adminName);
        recName.setText(name);
    }
    public void setLevel(String level){
        TextView t = mview.findViewById(R.id.adminLevel);
        t.setText(level);
    }
    public void setPassword(String password){
        TextView t = mview.findViewById(R.id.adminPassword);
        t.setText(password);
    }
    public void setEmail(String email){
        TextView t = mview.findViewById(R.id.adminEmail);
        t.setText(email);
    }
    public void setUID(String uid){
        TextView t = mview.findViewById(R.id.adminUID);
        t.setText(uid);
    }
}
