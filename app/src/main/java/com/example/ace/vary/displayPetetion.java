package com.example.ace.vary;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ace.vary.modelsAndAdapters.SignatureModel;
import com.example.ace.vary.modelsAndAdapters.petetionModel;
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

    TextView uid,name,dm,title,desc,signReq,totalSign,displaySignature;
    ImageView img;
    Button sign;

    private Uri filePath;
    private final int PICK_IMAGE_PEQUEST = 71;
    UserDataModel user;
    private String PetID;
    petetionModel pm,dispPet;
    int totSigns;

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
        signReq = findViewById(R.id.dispNoOfSign);
        title = findViewById(R.id.dispPetTitle);
        desc = findViewById(R.id.dispPetDesc);
        img = findViewById(R.id.dispPetImage);
        sign = findViewById(R.id.dispPetSign);
        //totalSign = findViewById(R.id.dispPetTotalSignatures);
        displaySignature = findViewById(R.id.DispSignatures);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Petetions").child(pm.uid).child(pm.pid);

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

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(displayPetetion.this)
                        .setTitle("Confirmation")
                        .setMessage("Please Confirm Your Signature")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Petetions/"+pm.uid+"/"+pm.getPid());
                              //  reference.child("Sign").child(user.getUserid()).setValue(user.getUserid());
                                DatabaseReference dr = FirebaseDatabase.getInstance().getReference().child("Signatures");
                                SignatureModel signatureModel = new SignatureModel();
                                signatureModel.pid = pm.pid;
                                signatureModel.title = dispPet.title;
                                signatureModel.uid = pm.uid;
                              //  Toast.makeText(displayPetetion.this, "Signature Confirmed", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(displayPetetion.this,otp.class);
                                intent.putExtra("user",user);
                                intent.putExtra("pete",signatureModel);
                                startActivity(intent);
                                //signThis();
                            }})
                        .setNegativeButton("NO", null).show();

            }
        });


    }



    private void loadImage() {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://vary-625b3.appspot.com").child(pm.uid).child(pm.getPid());
      /*

        Generate file download URL

        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.e("Tuts+", "uri: " + uri.toString());
                Toast.makeText(displayPetetion.this, uri.toString(), Toast.LENGTH_SHORT).show();

                //Handle whatever you're going to do with the URL here
            }
        });*/
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
        signReq.setText(dispPet.signatures);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Petetions/"+pm.uid+"/"+pm.getPid()).child("Sign");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                totSigns = (int) dataSnapshot.getChildrenCount();
                //Toast.makeText(displayPetetion.this, ""+totSigns, Toast.LENGTH_SHORT).show();
                displaySignature.setText(""+totSigns);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }


    private void signThis() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Petetions/"+pm.uid+"/"+pm.getPid());
        reference.child("Sign").child(user.getUserid()).setValue(user.getUserid());
        DatabaseReference dr = FirebaseDatabase.getInstance().getReference().child("Signatures");
        SignatureModel signatureModel = new SignatureModel();
        signatureModel.pid = pm.pid;
        signatureModel.title = dispPet.title;
        signatureModel.uid = pm.uid;
        dr.child(user.userid).child(pm.pid).setValue(signatureModel);


    }
}
