package com.example.ace.vary;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Profile extends AppCompatActivity {


    ImageView photo;
    Button upload,update;
    TextView name,id;
    EditText nameEdit,email,password;
    FirebaseStorage storage;
    private Uri filePath;
    StorageReference storageReference;
    FirebaseDatabase database ;
    DatabaseReference req ;
    int flg=0;
    UserDataModel user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        user = (UserDataModel) getIntent().getSerializableExtra("user");

        photo = findViewById(R.id.profileImage);
        upload = findViewById(R.id.ProfileImageUpload);
        update = findViewById(R.id.ProfileUpdate);
        name = findViewById(R.id.ProfileNameText);
        id = findViewById(R.id.ProfileIDText);
        nameEdit = findViewById(R.id.ProfileEditName);
        email = findViewById(R.id.ProfileEmail);
        password = findViewById(R.id.ProfilePass);

        database = FirebaseDatabase.getInstance();
        req = database.getReference("UserLogins");

        nameEdit.setText(user.name);
        email.setText(user.email);
        password.setText(user.pass);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        loadImage();

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
                uploadImage();

            }
        });

    }

    private void uploadImage() {

        if(filePath!=null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(Profile.this);
            progressDialog.setTitle("Uploading....");
            progressDialog.show();

            final StorageReference ref = storageReference.child(user.getUserid()+"/profile");
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(Profile.this, "Upload Complete", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(Profile.this, "Upload Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            int progress = (int) (100*((float)taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount()));
                            progressDialog.setMessage("Uploaded  "+progress+"%");




                        }
                    });


        }





    }

    private void loadImage() {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://vary-625b3.appspot.com").child(user.userid).child("profile");
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
                    photo.setImageBitmap(bitmap);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });
        } catch (IOException e ) { System.out.print(e.getMessage()); }


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 71 && resultCode == RESULT_OK
                && data!=null && data.getData()!=null){
            filePath = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                photo.setImageBitmap(bitmap);
            }   catch (IOException e){
                e.printStackTrace();
            }

        }
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Image"),71);


    }


    private void updateData() {

        req.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
            if(flg==0) {
                flg=1;
                Toast.makeText(Profile.this, "Updated Sucessfully", Toast.LENGTH_SHORT).show();
                UserDataModel updateUser = new UserDataModel(email.getText().toString(), user.level, nameEdit.getText().toString(), password.getText().toString(), user.userid);

                req.child(user.getUserid()).setValue(updateUser);
                user.email = email.getText().toString();
                user.name  = nameEdit.getText().toString();
                user.pass = password.getText().toString();
                Intent intent = new Intent(Profile.this, home.class);
                intent.putExtra("user", user);
                DatabaseReference.goOffline();
                Profile.this.startActivity(intent);
            }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






    }
}
