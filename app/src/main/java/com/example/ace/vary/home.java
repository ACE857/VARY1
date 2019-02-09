package com.example.ace.vary;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ace.vary.modelsAndAdapters.NewPetModel;
import com.example.ace.vary.modelsAndAdapters.petetionModel;
import com.example.ace.vary.modelsAndAdapters.recyclerPetetion;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class home extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    UserDataModel user;
    TextView textView;
    Intent intent;
    FirebaseRecyclerAdapter<NewPetModel, recyclerPetetion> adapter;
    FirebaseRecyclerOptions<NewPetModel> options;
    RecyclerView recyclerView;
    DatabaseReference mDatabase,query;
    ArrayList<NewPetModel> list;
    android.support.v7.widget.Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.homeRecView);

        user = (UserDataModel) getIntent().getSerializableExtra("user");
        Toast.makeText(this, user.getName(), Toast.LENGTH_SHORT).show();
        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.homeDrawerLayout);
        navigationView = findViewById(R.id.homeNavView);

        View head = navigationView.getHeaderView(0);
        textView = head.findViewById(R.id.headerUserId);
        textView.setText(user.getName());

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Petetions");
        //mDatabase.keepSynced(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));





        // assigning click listners to the navigation items
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override

            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.homeNavnewPet : item.setChecked(true);
                                                displayMessage("New Petetion Selected");
                                                intent = new Intent(home.this,NewPetetion.class);
                                                intent.putExtra("user",user);

                                                home.this.startActivity(intent);

                                                drawerLayout.closeDrawers();
                                                return true;
                    case R.id.homeMySign : item.setChecked(true);
                        displayMessage("My Signatures Selected");

                        intent = new Intent(home.this,MySignatures.class);
                        intent.putExtra("user",user);

                        home.this.startActivity(intent);

                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.homeMyPetet : item.setChecked(true);

                       /* intent = new Intent(home.this,displayPetetion.class);
                        intent.putExtra("user",user);
                        petetionModel openPet = new petetionModel();
                        openPet.setUid("16384");
                        openPet.setPid("1");
                        intent.putExtra("petetion",openPet);


                        home.this.startActivity(intent);

                        displayMessage("My Petetions Selected");*/


                        intent = new Intent(home.this,myPetetions.class);
                        intent.putExtra("user",user);
                        home.this.startActivity(intent);
                        displayMessage("My Petetions Selected");
                        drawerLayout.closeDrawers();
                        return true;



                    case R.id.homeProfile : item.setChecked(true);

                        intent = new Intent(home.this,Profile.class);
                        intent.putExtra("user",user);
                        home.this.startActivity(intent);


                        displayMessage("Profile Selected");
                        drawerLayout.closeDrawers();
                        return true;

                    case R.id.homeSuggestion: item.setChecked(true);
                        displayMessage("Suggestions Selected");


                        intent = new Intent(home.this,Suggestions.class);
                        intent.putExtra("user",user);
                        home.this.startActivity(intent);

                        drawerLayout.closeDrawers();
                        return true;

                    case R.id.homeLogout: item.setChecked(true);
                        displayMessage("Logout Selected");


                        intent = new Intent(home.this,MainActivity.class);
                        finish();
                        home.this.startActivity(intent);

                        drawerLayout.closeDrawers();
                        return true;

                }

                return false;
            }
        });

        // Recycler View

        query = FirebaseDatabase.getInstance().getReference().child("NewPetetion");
        options = new FirebaseRecyclerOptions.Builder<NewPetModel>()
                .setQuery(query, NewPetModel.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<NewPetModel, recyclerPetetion>(
                options) {
            @Override
            public recyclerPetetion onCreateViewHolder(ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_view, parent, false);


                return new recyclerPetetion(v);
            }

            @Override
            protected void onBindViewHolder(recyclerPetetion holder, final int position, final NewPetModel current) {


                holder.setName(current.name);
                holder.setTitle(current.title);
                holder.setImage(getApplicationContext(),current.pid,current.uid);

                holder.mview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(home.this, ""+current.pid, Toast.LENGTH_SHORT).show();
                        intent = new Intent(home.this,displayPetetion.class);
                        intent.putExtra("user",user);
                        petetionModel openPet = new petetionModel();
                        openPet.setUid(current.uid);
                        openPet.setPid(current.pid);
                        intent.putExtra("petetion",openPet);

                        home.this.startActivity(intent);
                    }
                });

                holder.mview.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {


                        return true;
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

    private void displayMessage(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
