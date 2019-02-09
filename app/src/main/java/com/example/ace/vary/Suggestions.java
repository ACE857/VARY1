package com.example.ace.vary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Suggestions extends AppCompatActivity {

    EditText sugg;
    Button submit;
    DatabaseReference reference;
    UserDataModel user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestions);

        sugg = findViewById(R.id.suggestionText);
        submit = findViewById(R.id.suggestionSubmit);
        reference = FirebaseDatabase.getInstance().getReference("Suggestions");

        user = (UserDataModel) getIntent().getSerializableExtra("user");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.child(user.userid).setValue(sugg.getText().toString());
                Toast.makeText(Suggestions.this, "Thank you for we value you suggestion and will work on it.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Suggestions.this,home.class);
                intent.putExtra("user",user);
                finish();
                startActivity(intent);
            }
        });

    }
}
