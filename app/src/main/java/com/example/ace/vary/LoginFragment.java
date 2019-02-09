package com.example.ace.vary;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    EditText id,pass;
    Button login;
    FirebaseDatabase database;
    String userLvl;
    DatabaseReference users;
    int flg=0;
    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragmen
        final View view = inflater.inflate(R.layout.login_fragment_xml, container, false);
        String [] values =
                {"Student","Teacher","Administrator"};
        Spinner spinner = (Spinner) view.findViewById(R.id.loginSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, values);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        id = view.findViewById(R.id.LoginId);
        pass = view.findViewById(R.id.LoginPass);
        login = view.findViewById(R.id.LoinButton);

        database = FirebaseDatabase.getInstance();
        users = database.getReference("UserLogins");

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flg=0;
                final ProgressDialog progressDialog = new ProgressDialog(view.getContext());
                progressDialog.setMessage("Logging You In");
                progressDialog.show();


                users.addValueEventListener(new ValueEventListener() {
                    @Override

                    public void onDataChange(DataSnapshot dataSnapshot) {
                        progressDialog.dismiss();

                        // checking if user exists
                        if (flg == 0){

                            if (dataSnapshot.child(id.getText().toString()).exists()) {
                                UserDataModel user = dataSnapshot.child(id.getText().toString()).getValue(UserDataModel.class);
                                user.setUserid(id.getText().toString());
                                // Toast.makeText(view.getContext(), ""+user.getLevel()+"-"+userLvl, Toast.LENGTH_SHORT).show();
                                if (user.getPass().equals(pass.getText().toString()) && userLvl.equals(user.getLevel())) {
                                    Toast.makeText(view.getContext(), "Welcome To VARY", Toast.LENGTH_SHORT).show();
                                    flg=1;
                                    // start home activity
                                    id.setText("");
                                    pass.setText("");
                                    if(userLvl.equals("Administrator")) {
                                        Intent intent = new Intent(view.getContext(), Administrator.class);
                                        intent.putExtra("user", user);
                                        startActivity(intent);
                                    }
                                    else {
                                        Intent intent = new Intent(view.getContext(), home.class);
                                        intent.putExtra("user", user);
                                        startActivity(intent);
                                    }



                                } else
                                    Toast.makeText(view.getContext(), "Wrong Password Or Wrong Lvl Of Authentication", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(view.getContext(), "UserId Invalid", Toast.LENGTH_SHORT).show();
                            }
                    }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });
            }
        });


        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String msg = parent.getItemAtPosition(position).toString();
        userLvl = msg;

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
