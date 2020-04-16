package com.example.payse;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class SignUpActivity extends AppCompatActivity {

    //    private FirebaseAuth mAuth;
    private EditText phone, password, username;
    //    verifyCode;
//    private Button verify;
    private CardView getstarted;
    //    private boolean verifying;
//    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
//    private String mVerificationId,
    private String phoneNumber;
    FirebaseFirestore db;
    Map<String, String> user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        getstarted = findViewById(R.id.getstarted);
        username = findViewById(R.id.uname1);
        db = FirebaseFirestore.getInstance();
//        verifying = false;
//        mAuth = FirebaseAuth.getInstance();
//        signup = findViewById(R.id.signup);
        phone = findViewById(R.id.phnumber);
        password = findViewById(R.id.pin);
        username = findViewById(R.id.uname1);
//        verifyCode = findViewById(R.id.verifycode);
//        verify = findViewById(R.id.verify);
        user = new HashMap<>();


//        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//            @Override
//            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
//                //Getting the code sent by SMS
//                String code = phoneAuthCredential.getSmsCode();
//
//                //sometime the code is not detected automatically
//                //in this case the code will be null
//                //so user has to manually enter the code
//                if (code != null) {
//                    verifyCode.setText(code);
//                    //verifying the code
//                    verifyVerificationCode(code);
//                }
//            }
//
//            @Override
//            public void onVerificationFailed(FirebaseException e) {
//                Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//                super.onCodeSent(s, forceResendingToken);
//                mVerificationId = s;
//                PhoneAuthProvider.ForceResendingToken mResendToken = forceResendingToken;
//            }
//        };
//

        getstarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean flag = true;
                String phoneNumber = "+91" + phone.getText().toString();
                if (phoneNumber.length() != 13) {
                    Toast.makeText(SignUpActivity.this, "Enter a valid phone number.", Toast.LENGTH_LONG).show();
                    flag = false;
                }
                String pin = password.getText().toString();
                if (pin.length() != 4) {
                    Toast.makeText(SignUpActivity.this, "Enter a 4 digit PIN", Toast.LENGTH_LONG).show();
                    flag = false;
                }
                String name = username.getText().toString();
                if (name.length() == 0) {
                    Toast.makeText(SignUpActivity.this, "Enter name", Toast.LENGTH_LONG).show();
                    flag = false;
                }
//                Query query = db.collection("users").whereEqualTo("phone",phoneNumber);
//                if(query != null){
//                    Intent intent = new Intent(SignUpActivity.this, payment_options.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                    intent.putExtra("phone",phone.getText().toString());
//                    intent.putExtra("phone", phoneNumber);
//                    intent.putExtra("name", name);
//                    intent.putExtra("pin", pin);
//                    intent.putExtra("balance", "500");
//                    flag = false;
//                    startActivity(intent);
//                }
//                ArrayList<Integer> prev_transactions = new ArrayList<>(10);
//                prev_transactions.add(500);
                if (flag) {
//                    user.put("phone", phoneNumber);
//                    user.put("name", name);
//                    user.put("pin", pin);
//                    user.put("balance", "500");
//                    user.put("prev_transactions",prev_transactions.);

//                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                            phoneNumber,        // Phone number to verify
//                            60,                 // Timeout duration
//                            TimeUnit.SECONDS,   // Unit of timeout
//                            SignUpActivity.this,               // Activity (for callback binding)
//                            mCallbacks);
//                    db.collection("users").document(phoneNumber).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//                            Log.d("User creation success", "DocumentSnapshot added with ID: " + phone.getText().toString());
//                            Toast.makeText(SignUpActivity.this, "Signup successful.Restart the app to login.", Toast.LENGTH_LONG).show();
//                        }
//                    })
//                            .addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    Log.w("User creation failed", "Error adding document", e);
//                                }
//                            });
                    Intent intent = new Intent(SignUpActivity.this, payment_options.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("phone", phoneNumber);
                    intent.putExtra("name", name);
                    intent.putExtra("pin", pin);
                    intent.putExtra("flag",-1);
                    startActivity(intent);
                    finish();
                } else
                    Toast.makeText(SignUpActivity.this, "Enter valid credentials.", Toast.LENGTH_LONG).show();
            }
        });
    }
}
//
//        verify.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mCallbacks.onVerificationCompleted();
//            }
//        });
//    }

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
//
//    private void verifyVerificationCode(String otp) {
//        //creating the credential
//        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otp);
//        //signing the user
//        signInWithPhoneAuthCredential(credential);
//
//    }

//    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            //verification successful we will start the profile activity
//                            Intent intent = new Intent(SignUpActivity.this, payment_options.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
////                            intent.putExtra("username",username.getText().toString());
//                            startActivity(intent);
//                            Toast.makeText(SignUpActivity.this,"Login successful",Toast.LENGTH_LONG);
//
//                        } else {
//
//                            //verification unsuccessful.. display an error message
//
//                            String message = "Somthing is wrong, we will fix it soon...";
//
//                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
//                                message = "Invalid Code.";
//                            }
//                            Toast.makeText(SignUpActivity.this,message,Toast.LENGTH_LONG).show();
//                        }
//                    }
//                });
//    }
//}