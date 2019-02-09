package com.example.ace.vary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ace.vary.modelsAndAdapters.SignatureAdapter;
import com.example.ace.vary.modelsAndAdapters.SignatureModel;
import com.example.ace.vary.modelsAndAdapters.petetionModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MySignatures extends AppCompatActivity {

    UserDataModel user;
    TextView title,userid;
    Intent intent;
    FirebaseRecyclerAdapter<SignatureModel, SignatureAdapter> adapter;
    FirebaseRecyclerOptions<SignatureModel> options;
    RecyclerView recyclerView;
    DatabaseReference query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_signatures);

        user = (UserDataModel) getIntent().getSerializableExtra("user");

        title = findViewById(R.id.signTitle);
        userid = findViewById(R.id.signUID);
        recyclerView = findViewById(R.id.signRecView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        query = FirebaseDatabase.getInstance().getReference().child("Signatures").child(user.getUserid());

        options = new FirebaseRecyclerOptions.Builder<SignatureModel>()
                .setQuery(query, SignatureModel.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<SignatureModel, SignatureAdapter>(
                options) {
            @Override
            public SignatureAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_signatures, parent, false);


                return new SignatureAdapter(v);
            }

            @Override
            protected void onBindViewHolder(SignatureAdapter holder, final int position, final SignatureModel current) {


                holder.setTitle(current.title);
                holder.setUID(current.uid);

                holder.mview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        intent = new Intent(MySignatures.this,displayPetetion.class);
                        intent.putExtra("user",user);
                        petetionModel openPet = new petetionModel();
                        openPet.setUid(current.uid);
                        openPet.setPid(current.pid);
                        intent.putExtra("petetion",openPet);

                        MySignatures.this.startActivity(intent);
                    }
                });
            }


        };

        //Populate Item into Adapter
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();



    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }


}
