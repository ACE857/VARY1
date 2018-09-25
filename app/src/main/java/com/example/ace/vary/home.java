package com.example.ace.vary;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

public class home extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    UserDataModel user;
    TextView textView;
    Intent intent;

    android.support.v7.widget.Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        user = (UserDataModel) getIntent().getSerializableExtra("user");
        Toast.makeText(this, user.getName(), Toast.LENGTH_SHORT).show();
        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.homeDrawerLayout);
        navigationView = findViewById(R.id.homeNavView);

        View head = navigationView.getHeaderView(0);
        textView = head.findViewById(R.id.headerUserId);
        textView.setText(user.getName());

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
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.homeMyPetet : item.setChecked(true);

                        intent = new Intent(home.this,displayPetetion.class);
                        intent.putExtra("user",user);
                        petetionModel openPet = new petetionModel();
                        openPet.setUid("16384");
                        openPet.setPid("1");
                        intent.putExtra("petetion",openPet);


                        home.this.startActivity(intent);

                        displayMessage("My Petetions Selected");
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.homeProfile : item.setChecked(true);
                        displayMessage("Profile Selected");
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.homeLogout: item.setChecked(true);
                        displayMessage("Logout Selected");
                        drawerLayout.closeDrawers();
                        return true;

                }

                return false;
            }
        });

    }

    private void displayMessage(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
