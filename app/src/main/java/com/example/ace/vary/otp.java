package com.example.ace.vary;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ace.vary.modelsAndAdapters.SignatureModel;
import com.example.ace.vary.modelsAndAdapters.petetionModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class otp extends AppCompatActivity {

    UserDataModel user;
    private FirebaseAuth mAuth;
    SignatureModel sign;
    ProgressBar progressBar;
    private String verificationId;
    private EditText editTextCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressbar);
        editTextCode = findViewById(R.id.editTextCode);
        user = (UserDataModel) getIntent().getSerializableExtra("user");
        sign = (SignatureModel) getIntent().getSerializableExtra("pete");
        Toast.makeText(this, ""+user.email, Toast.LENGTH_SHORT).show();
        sendVerificationCode(user.email);
        findViewById(R.id.buttonSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = editTextCode.getText().toString().trim();

                if (code.isEmpty() || code.length() < 6){
                    editTextCode.setError("Enter code...");
                    editTextCode.requestFocus();
                    return;
                }
                verifyCode(code);
            }
        });



    }

    private void verifyCode(String code){
        try{
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            signInWithCredential(credential);
        }catch (Exception e){
            Log.e("error",e.getMessage());
        }

    }

    private void signInWithCredential(PhoneAuthCredential credential){
        try{
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Intent intent = new Intent(otp.this, home.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.putExtra("user", user);
                                oksign();
                                Toast.makeText(otp.this, "Thank You For Your Signature.", Toast.LENGTH_SHORT).show();
                                startActivity(intent);
                            }else {
                                Toast.makeText(getApplicationContext(), task.getException().toString()
                                        , Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }catch (Exception e){
            Log.e("error",e.getMessage());
        }

    }

    private void sendVerificationCode(String number){
        try{
            progressBar.setVisibility(View.VISIBLE);
            number = "+91"+number;
            Log.e("error",number);
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    number,
                    60,
                    TimeUnit.SECONDS,
                    TaskExecutors.MAIN_THREAD,
                    mCallBack
            );
            Log.e("error","OTP SENT");
        }catch (Exception e){
            Log.e("error",e.getMessage());
        }
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            try{
                super.onCodeSent(s, forceResendingToken);
                verificationId = s;
            }catch (Exception e){
                Log.e("error",e.getMessage());
            }

        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            editTextCode.setText(code);
            if (code != null){
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    };

    void oksign(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Petetions/"+sign.uid+"/"+sign.pid);
        reference.child("Sign").child(user.getUserid()).setValue(user.getUserid());
        DatabaseReference dr = FirebaseDatabase.getInstance().getReference().child("Signatures");
        dr.child(user.userid).child(sign.uid).setValue(sign);
    }
}
