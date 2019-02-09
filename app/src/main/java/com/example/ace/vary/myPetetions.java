package com.example.ace.vary;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ace.vary.modelsAndAdapters.petetionModel;
import com.example.ace.vary.modelsAndAdapters.recyclerPetetion;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class myPetetions extends AppCompatActivity {


    NavigationView navigationView;
    UserDataModel user;
    TextView textView;
    Intent intent;
    FirebaseRecyclerAdapter<petetionModel, recyclerPetetion> adapter;
    FirebaseRecyclerOptions<petetionModel> options;
    RecyclerView recyclerView;
    DatabaseReference mDatabase,query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_petetions);

        recyclerView = findViewById(R.id.myRecView);

        user = (UserDataModel) getIntent().getSerializableExtra("user");

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Petetions");
        //mDatabase.keepSynced(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        query = FirebaseDatabase.getInstance().getReference().child("Petetions").child(user.getUserid());

        options = new FirebaseRecyclerOptions.Builder<petetionModel>()
                .setQuery(query, petetionModel.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<petetionModel, recyclerPetetion>(
                options) {
            @Override
            public recyclerPetetion onCreateViewHolder(ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_view, parent, false);


                return new recyclerPetetion(v);
            }

            @Override
            protected void onBindViewHolder(recyclerPetetion holder, final int position, final petetionModel current) {

                holder.setName(current.name);
                holder.setTitle(current.title);
                holder.setImage(getApplicationContext(),current.pid,current.uid);

                holder.mview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(myPetetions.this, ""+position, Toast.LENGTH_SHORT).show();
                        intent = new Intent(myPetetions.this,displayPetetion.class);
                        intent.putExtra("user",user);
                        petetionModel openPet = new petetionModel();
                        openPet.setUid(current.uid);
                        openPet.setPid(current.pid);
                        intent.putExtra("petetion",openPet);

                        myPetetions.this.startActivity(intent);
                    }
                });
            }


        };

        //Populate Item into Adapter
        recyclerView.setAdapter(adapter);



    }

    public static void clicked(int pos){

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
