package com.example.ace.vary;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ace.vary.modelsAndAdapters.NewPetModel;
import com.example.ace.vary.modelsAndAdapters.petetionModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class NewPetetion extends AppCompatActivity {

    private String info,date;
    EditText title,sol,signThresh;
    AutoCompleteTextView decMaker;
    ImageView photo;
    Button submit,choose;
    private Uri filePath;
    private final int PICK_IMAGE_PEQUEST = 71;
    UserDataModel user;
    private String PetID;
    com.example.ace.vary.modelsAndAdapters.petetionModel petetionModel;
    FirebaseStorage storage;
    StorageReference storageReference;
    int chk=0;
    FirebaseDatabase database ;
    ArrayList hintName = new ArrayList();
    ArrayList hintID = new ArrayList();
    DatabaseReference req ,newreq;
    String imgUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_petetion);
        fillLang();
        user = (UserDataModel) getIntent().getSerializableExtra("user");

        title = findViewById(R.id.newPetTitle);
        decMaker = findViewById(R.id.newPetDM);
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,hintName);

        decMaker.setAdapter(adapter);
        decMaker.setThreshold(1);

        sol = findViewById(R.id.newPetSol);
        photo = findViewById(R.id.newPetImg);
        submit = findViewById(R.id.newPetSumbit);
        signThresh = findViewById(R.id.netPetSignThresh);
        choose = findViewById(R.id.newPetuploadBtn);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        database = FirebaseDatabase.getInstance();
        req = database.getReference("Petetions");
        newreq = database.getReference("NewPetetion");

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(peformCheck()) {
                    AlertDialog alertDialog = new AlertDialog.Builder(
                            NewPetetion.this).create();
                    alertDialog.setTitle("oop's Empty Fields");
                    alertDialog.setMessage("Please fill all fields and select an image and Signatures should be valid ...");
                    alertDialog.setIcon(R.drawable.info);
                    alertDialog.show();

                     }
                     else
                    uploadData();


            }
        });


    }

    void fillLang(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("UserLogins");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dsn : dataSnapshot.getChildren()){
                    UserDataModel um = dsn.getValue(UserDataModel.class);
                    hintName.add(um.name);
                    hintName.add(um.userid);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        chk=0;
    }

    @Override
    protected void onResume() {
        super.onResume();
        chk=0;
    }

    private boolean peformCheck() {

        Boolean bl = title.getText().toString().length() <= 0 || decMaker.getText().toString().length() <= 0 || sol.getText().toString().length() <= 0 || filePath == null;

        if(!hintName.contains(decMaker.getText()+"")) { bl = true;
            Toast.makeText(this, "Decision Maker Invalid !", Toast.LENGTH_SHORT).show(); }
        return bl;


    }

    @SuppressLint("SimpleDateFormat")
    private void uploadData() {

            final ProgressDialog progressDialog = new ProgressDialog(NewPetetion.this);
            progressDialog.setMessage("Uploading..");
            progressDialog.show();
         date = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            req.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    progressDialog.dismiss();
                    if(chk==0) {
                        chk = 1;
                        // getting a petetion id for this petetion  count is totoal no of petetion till now
                        int count = dataSnapshot.child("count").getValue(Integer.class);
                        count--;
                        PetID = "" + count;
                        req.child("count").setValue(count);
                        //  updating value of count in databasex
                        uploadImage(count);


                        petetionModel = new petetionModel(user.getUserid(), PetID, title.getText().toString(), decMaker.getText().toString(), sol.getText().toString(), PetID, user.getName(), signThresh.getText().toString(), date);

                        req.child(user.getUserid()).child(PetID).setValue(petetionModel);

                        Intent intent = new Intent(NewPetetion.this, home.class);
                        intent.putExtra("user", user);
                        DatabaseReference.goOffline();
                        NewPetetion.this.startActivity(intent);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        newreq.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                NewPetModel npm = new NewPetModel(petetionModel.imageID,petetionModel.name,petetionModel.pid,petetionModel.title,petetionModel.uid);
                newreq.child(petetionModel.pid).setValue(npm);
                DatabaseReference.goOffline();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }

    private void uploadImage(int count) {

        if(filePath!=null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(NewPetetion.this);
            progressDialog.setTitle("Uploading....");
            progressDialog.show();

            final StorageReference ref = storageReference.child(user.getUserid()+"/"+PetID);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(NewPetetion.this, "Upload Complete", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(NewPetetion.this, "Upload Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Image"),PICK_IMAGE_PEQUEST);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_PEQUEST && resultCode == RESULT_OK
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

    public void showInfoTitle(View view){
        info = "\nThis is the first thing people will see about your petition. Get their attention with a short title that focusses on the change you’d like them to support.\n" +
                "\n1.Keep it short and to the point\n" +
                "\n2.Focus on the solution\n" +
                "\n3.Communicate urgency\n";

        AlertDialog alertDialog = new AlertDialog.Builder(
                view.getContext()).create();
        alertDialog.setTitle("The Title");
        alertDialog.setMessage(info);
        alertDialog.setIcon(R.drawable.info);
        alertDialog.show();

    }
    public void showInfoDec(View view){
        info = "This is the person, organisation, or group that can make a decision about your petition. Change.org will send them updates on your petition and encourage a response.\n" +
                "\n1. Choose someone who can give you what you want\n" +
                "\n2. Don't go straight to the top\n" +
                "\n3. Choose someone you can work with\n";
        AlertDialog alertDialog = new AlertDialog.Builder(
                view.getContext()).create();
        alertDialog.setTitle("The Decesion Maker");
        alertDialog.setMessage(info);
        alertDialog.setIcon(R.drawable.info);
        alertDialog.show();

    }
    public void showInfoSol(View view){
        info = "People are more likely to support your petition if it’s clear why you care. Explain how this change will impact you, your family, or your community.\n" +
                "\n1. Describe the people involved and the problem they are facing\n" +
                "\n2. Describe the solution\n" +
                "\n3. Make it personal\n" +
                "\n4. Respect others\n";
        AlertDialog alertDialog = new AlertDialog.Builder(
                view.getContext()).create();
        alertDialog.setTitle("The Solution");
        alertDialog.setMessage(info);
        alertDialog.setIcon(R.drawable.info);
        alertDialog.show();

    }
    public void showInfoPhoto(View view){
        info = "Petitions with a photo or video receive six times more signatures than those without. Include one that captures the emotion of your story.\n" +
                "\n1. Choose a photo that captures the emotion of your petition\n" +
                "\n2. Try to upload photos that are relevant and appealing\n" +
                "\n3. Keep it friendly for all audiences\n" +
                "\n4. Make sure your photo doesn't include graphic violence or sexual content.\n";
        AlertDialog alertDialog = new AlertDialog.Builder(
                view.getContext()).create();
        alertDialog.setTitle("The Image");
        alertDialog.setMessage(info);
        alertDialog.setIcon(R.drawable.info);
        alertDialog.show();
    }

}
