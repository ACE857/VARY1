package com.example.ace.vary.modelsAndAdapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.ace.vary.R;

public class SignatureAdapter extends RecyclerView.ViewHolder  {
    public View mview;
    public SignatureAdapter(View itemView) {
        super(itemView);
        mview = itemView;

    }

    public void setTitle(String title){
        TextView recName = mview.findViewById(R.id.signTitle);
        recName.setText(title);
    }
    public void setUID(String uid){
        TextView t = mview.findViewById(R.id.signUID);
        t.setText(uid);
    }
}
