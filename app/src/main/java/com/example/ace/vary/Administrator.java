package com.example.ace.vary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ace.vary.modelsAndAdapters.AdminAdapter;
import com.example.ace.vary.modelsAndAdapters.AdminModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Administrator extends AppCompatActivity {

    UserDataModel user;
    TextView name,email,uid,password,level;
    Intent intent;
    Button approve,reject;
    FirebaseRecyclerAdapter<AdminModel, AdminAdapter> adapter;
    FirebaseRecyclerOptions<AdminModel> options;
    RecyclerView recyclerView;
    DatabaseReference query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator);
        name = findViewById(R.id.adminName);
        password = findViewById(R.id.adminPassword);
        email = findViewById(R.id.adminEmail);
        uid = findViewById(R.id.adminUID);
        level = findViewById(R.id.adminLevel);
        approve = findViewById(R.id.adminApprove);
        reject = findViewById(R.id.adminReject);

        recyclerView = findViewById(R.id.adminRec);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        query = FirebaseDatabase.getInstance().getReference().child("SignUpRequests");

        options = new FirebaseRecyclerOptions.Builder<AdminModel>()
                .setQuery(query, AdminModel.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<AdminModel, AdminAdapter>(
                options) {
            @Override
            public AdminAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_rec, parent, false);


                return new AdminAdapter(v);
            }

            @Override
            protected void onBindViewHolder(final AdminAdapter holder, final int position, final AdminModel current) {

                    holder.setEmail(current.email);
                    holder.setLevel(current.level);
                    holder.setName(current.name);
                    holder.setPassword(current.pass);
                    holder.setUID(current.userid);

                holder.approve.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseReference req = FirebaseDatabase.getInstance().getReference("UserLogins");
                        UserDataModel user = new UserDataModel(current.email,current.level,current.name,current.pass,current.userid);
                        req.child(current.userid).setValue(user);
                        DatabaseReference del = FirebaseDatabase.getInstance().getReference("SignUpRequests");
                        del.child(current.userid).removeValue();
                    }

                });

                holder.reject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(Administrator.this, "reject works", Toast.LENGTH_SHORT).show();
                        DatabaseReference del = FirebaseDatabase.getInstance().getReference("SignUpRequests");
                        del.child(current.userid).removeValue();
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
