package com.example.ace.vary;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;



import java.io.File;
import java.io.IOException;

public class displayPetetion extends AppCompatActivity {
    
    TextView uid,name,dm,title,desc;
    ImageView img;
    Button sign;

    private Uri filePath;
    private final int PICK_IMAGE_PEQUEST = 71;
    UserDataModel user;
    private String PetID;
    petetionModel pm,dispPet;

    FirebaseStorage storage;
    StorageReference storageReference;

    FirebaseDatabase database ;
    DatabaseReference ref ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_petetion);

        user = (UserDataModel) getIntent().getSerializableExtra("user");        //  details of the user that created the petetion
        pm = (petetionModel) getIntent().getSerializableExtra("petetion");      // pm contanins details like pid and decesion maker of petetion to be displayed
        
        uid = findViewById(R.id.dispPetUserID);
        name = findViewById(R.id.dispPetUserName);
        dm = findViewById(R.id.dispPetDM);
        title = findViewById(R.id.dispPetTitle);
        desc = findViewById(R.id.dispPetDesc);
        img = findViewById(R.id.dispPetImage);
        sign = findViewById(R.id.dispPetSign);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Petetions/"+user.getUserid()+"/"+pm.getPid());

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dispPet = dataSnapshot.getValue(petetionModel.class);
                setValues();
                loadImage();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


    }

    private void loadImage() {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://vary-625b3.appspot.com").child(user.getUserid()).child(pm.getPid());
        try {
            final File localFile = File.createTempFile("images", "jpg");
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    img.setImageBitmap(bitmap);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });
        } catch (IOException e ) { System.out.print(e.getMessage()); }


    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    private void setValues() {
        uid.setText(dispPet.uid);
        name.setText(dispPet.name);
        dm.setText(dispPet.decesionMaker);
        title.setText(dispPet.title);
        desc.setText(dispPet.solution);

    }


    private void signThis() {
    }
}
