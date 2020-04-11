package com.example.payse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class nav_header extends AppCompatActivity {

    private TextView phno;
    private TextView pname;
    private String phonenumber;
    private String name;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_header);
        phno = findViewById(R.id.phno);
        pname = findViewById(R.id.proname);
//        db = FirebaseFirestore.getInstance();
//        getData(db);
    }

    public void getData(FirebaseFirestore db){

        phno = findViewById(R.id.phno);
        pname = findViewById(R.id.proname);
        final DocumentSnapshot[] document = new DocumentSnapshot[1];
            Task<DocumentSnapshot> user = db.collection("users").document(phonenumber).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> user) {
                    if (user.isSuccessful()) {
                        document[0] = user.getResult();
                        if (document[0].exists()) {
                            Log.d("Firestore","Document retrieved");
                        } else {
                            Log.d("Firestore", "No such document");
                        }
                    } else {
                        Log.d("Firestore error", "get failed with ", user.getException());
                    }
                }
                });
        phonenumber = String.valueOf(document[0].get("phone"));
        name = String.valueOf(document[0].get("name"));
        phno.setText(phonenumber);
        pname.setText(name);
    }
}
