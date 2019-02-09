package com.example.ace.vary.modelsAndAdapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ace.vary.R;
import com.example.ace.vary.UserDataModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

public class recyclerPetetion extends RecyclerView.ViewHolder {


    public View mview;
    public recyclerPetetion(View itemView) {
        super(itemView);
        mview = itemView;

    }
    public void setName(String name){
        TextView recName = mview.findViewById(R.id.recUsrName);
        recName.setText(name);
    }
    public void setTitle(String title){
        TextView t = mview.findViewById(R.id.recUsrTitle);
        t.setText(title);
    }
    public void setImage(Context ctx, String image,String user){
        final ImageView iv = mview.findViewById(R.id.recImg);
        /*Picasso.with(ctx).load(image).into(iv);*/

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference().child(user+"/"+image);
        try {
            final File localFile = File.createTempFile("images", "jpg");
            storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    iv.setImageBitmap(bitmap);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });
        } catch (IOException e ) {}

    }

}

