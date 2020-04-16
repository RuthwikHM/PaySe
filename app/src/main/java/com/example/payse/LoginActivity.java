package com.example.payse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirestoreRegistrar;


public class LoginActivity extends AppCompatActivity {
    private EditText phone,et1,et2,et3,et4;
    private CardView login;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        phone = findViewById(R.id.phone);
        et1 = findViewById(R.id.et1);
        et2 = findViewById(R.id.et2);
        et3 = findViewById(R.id.et3);
        et4 = findViewById(R.id.et4);;
        login = findViewById(R.id.login);
        db = FirebaseFirestore.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String phonenumber = "+91" + phone.getText().toString();
                Task<DocumentSnapshot> userdata = db.collection("users").document(phonenumber).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> user) {
                        if (user.isSuccessful()) {
                            DocumentSnapshot document;
                            document = user.getResult();
                           if(document.exists()){
                               String pin = et4.getText().toString();
                               pin = pin.concat(et3.getText().toString());
                               pin = pin.concat(et2.getText().toString());
                               pin = pin.concat(et1.getText().toString());
                               String val_pin = String.valueOf(document.get("pin"));
                               if(pin.equals(val_pin)){
                                   Intent intent = new Intent(LoginActivity.this,payment_options.class);
                                   intent.putExtra("phone", phonenumber);
                                   intent.putExtra("name", String.valueOf(document.get("name")));
                                   intent.putExtra("pin", String.valueOf(document.get("pin")));
                                   Toast.makeText(LoginActivity.this,"Login Successful.",Toast.LENGTH_LONG).show();
                                   startActivity(intent);
                                   finish();
                               }
                               else Toast.makeText(LoginActivity.this,"Invalid PIN.",Toast.LENGTH_LONG).show();
                           }
                           else {
                               Toast.makeText(LoginActivity.this, "User does not exist.Please signup before logging in.", Toast.LENGTH_LONG).show();
                               startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
                           }
                        } else {
                            Log.d("Firestore error", "get failed with ", user.getException());
                        }
                    }
                });

            }
        });
    }
}
