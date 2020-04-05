package com.example.payse;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText phone,password,username,verifyCode;
    private Button verify;
    private CardView getstarted;
    //    private boolean verifying;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;
    FirebaseFirestore db;
    Map<String, Object> user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.signup);


        setContentView(R.layout.activity_main);
        getstarted = findViewById(R.id.loginCard);
        username = findViewById(R.id.uname1);

//        getstarted.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i1 = new Intent(SignUpActivity.this, payment_options.class);
//                i1.putExtra("full_name",fullName);
//                startActivity(i1);
//            }
//        });

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
//        signup = findViewById(R.id.signup);
        phone = findViewById(R.id.phnumber);
        password = findViewById(R.id.pin);
        username = findViewById(R.id.uname1);
        verifyCode = findViewById(R.id.verifycode);
        verify = findViewById(R.id.verify);
        user = new HashMap<>();


        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                //Getting the code sent by SMS
                String code = phoneAuthCredential.getSmsCode();

                //sometime the code is not detected automatically
                //in this case the code will be null
                //so user has to manually enter the code
                if (code != null) {
                    verifyCode.setText(code);
                    //verifying the code
                    verifyVerificationCode(code);
                }
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                mVerificationId = s;
                PhoneAuthProvider.ForceResendingToken mResendToken = forceResendingToken;
            }
        };


        getstarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verify = findViewById(R.id.verify);
                verifyCode = findViewById(R.id.verifycode);
                verify.setVisibility(View.VISIBLE);
                verifyCode.setVisibility(View.VISIBLE);
                String phoneNumber = "+91" + phone.getText().toString();
                String pin = password.getText().toString();
                String name = username.getText().toString();
                ArrayList<Integer> prev_transactions = new ArrayList<Integer>(10);
                prev_transactions.add(500);
                if(phoneNumber.length() == 13 && pin.length() != 0 && pin.length() != 4 && name.length() != 0 ) {
                    user.put("phone", phoneNumber);
                    user.put("name", name);
                    user.put("pin", pin);
                    user.put("balance",500);
                    user.put("prev_transactions",prev_transactions);

                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            phoneNumber,        // Phone number to verify
                            60,                 // Timeout duration
                            TimeUnit.SECONDS,   // Unit of timeout
                            SignUpActivity.this,               // Activity (for callback binding)
                            mCallbacks);
                }
                else Toast.makeText(SignUpActivity.this,"Enter valid credentials.",Toast.LENGTH_LONG).show();
            }
        });
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser == null){
//            Intent notLoggedIn = new Intent(SignUpActivity.this,LoginActivity.class);
//            startActivity(notLoggedIn);
//            finish();
//        }
//    }

    private void verifyVerificationCode(String otp) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otp);
        //signing the user
        signInWithPhoneAuthCredential(credential);
        db.collection("users").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d("User creation success", "DocumentSnapshot added with ID: " + documentReference.getId());
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("User creation failed", "Error adding document", e);
                    }
                });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //verification successful we will start the profile activity
                            Intent intent = new Intent(SignUpActivity.this, payment_options.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            Toast.makeText(SignUpActivity.this,"Login successful",Toast.LENGTH_LONG);

                        } else {

                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid Code.";
                            }
                            Toast.makeText(SignUpActivity.this,message,Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
