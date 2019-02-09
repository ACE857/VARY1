package com.example.ace.vary;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class SignUpFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    String userLvl;
    EditText signid,signname,signemail,signpass,signrepass;
    Button signup;
    FirebaseDatabase database ;
    int flg=0;
    DatabaseReference req ;
    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.sign_fragment_xml, container, false);

        String [] values =
                {"Student","Teacher"};
        Spinner spinner = (Spinner) view.findViewById(R.id.loginSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, values);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        signid = view.findViewById(R.id.signupUserId);
        signname = view.findViewById(R.id.signupName);
        signemail = view.findViewById(R.id.signupEmail);
        signpass = view.findViewById(R.id.signPass);
        signrepass = view.findViewById(R.id.signPassre);
        signup = view.findViewById(R.id.sign);

        database = FirebaseDatabase.getInstance();
        req = database.getReference("SignUpRequests");

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flg=0;
                final ProgressDialog progressDialog = new ProgressDialog(view.getContext());
                progressDialog.setMessage("Signing You In...");
                progressDialog.show();

                req.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(flg==0){
                            flg=1;
                        progressDialog.dismiss();
                        if (dataSnapshot.child(signid.getText().toString()).exists()) {
                            Toast.makeText(view.getContext(), "Pending Request", Toast.LENGTH_SHORT).show();
                        } else {
                            if (signpass.getText().toString().equals(signrepass.getText().toString())) {
                                UserDataModel user = new UserDataModel(signemail.getText().toString(), userLvl, signname.getText().toString(), signpass.getText().toString(),signid.getText().toString());
                                req.child(signid.getText().toString()).setValue(user);
                                Toast.makeText(view.getContext(), "Request Submitted Sucessfully", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(view.getContext(), "Password Do Not Match", Toast.LENGTH_SHORT).show();
                        }

                    }
                    signemail.setText("");
                        signid.setText(""); signname.setText("");   signpass.setText(""); signrepass.setText("");
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
