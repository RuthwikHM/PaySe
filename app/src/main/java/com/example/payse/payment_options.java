package com.example.payse;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class payment_options extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private TextView pname, phno,balance;
    String name, phonenumber,bal;
    //    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Task<DocumentSnapshot> userdata;
    private IntentIntegrator qrScan;
    private String to_phonenumber;
    private ImageView generate,makepay;
    ArrayList prev_trans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_options);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        mAuth = FirebaseAuth.getInstance();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
        prev_trans = new ArrayList<Integer>(10);
        makepay = findViewById(R.id.pay);
        generate = findViewById(R.id.receive);

        db = FirebaseFirestore.getInstance();
        qrScan = new IntentIntegrator(this);
        qrScan.setPrompt("Scan the QR code.");
        qrScan.setOrientationLocked(true);
//        qrScan.setTimeout(2000);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_draw_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent recieve = new Intent(payment_options.this, RecievePayment.class);
                recieve.putExtra("phone", phonenumber);
                startActivity(recieve);
            }
        });


        makepay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qrScan.initiateScan();
            }
        });

    }
        public void update_DB(FirebaseFirestore db, Map<String, Object> data) {
        db.collection("users").document(phonenumber).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("User creation success", "DocumentSnapshot added with ID: " + phonenumber);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("User creation failed", "Error adding document", e);
            }
        });
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_transactions:
                Intent recent = new Intent(payment_options.this, recent_payments.class);
                recent.putExtra("prev_transactions",prev_trans);
                startActivity(recent);
                break;
//            case R.id.pay:
//                qrScan.initiateScan();
//                break;
//            case R.id.recieve:
//                Intent recieve = new Intent(payment_options.this,RecievePayment.class);
//                recieve.putExtra("phone",phonenumber);
//                startActivity(recieve);
//                break;
        }
        return true;
    }



    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        phonenumber = getIntent().getStringExtra("phone");
        name = getIntent().getStringExtra("name");
        String pin = getIntent().getStringExtra("pin");
        int flag = getIntent().getIntExtra("transaction_status",-1);
        prev_trans.add(500);
        final Map<String, Object> data = new HashMap<>();
        data.put("phone", phonenumber);
        data.put("name", name);
        data.put("pin", pin);
        data.put("balance", "500");
        data.put("prev_transactions",prev_trans);
        if(flag == 0) Toast.makeText(payment_options.this,"Transaction successful",Toast.LENGTH_LONG).show();
        final DocumentSnapshot[] document = new DocumentSnapshot[1];
        userdata = db.collection("users").document(phonenumber).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> user) {
                if (user.isSuccessful()) {
                    document[0] = user.getResult();
                    boolean exists = document[0].exists();
//                    if (document[0].exists()) {
//                        Log.d("Firestore","Document retrieved");
//                    } else {
//                        Log.d("Firestore", "No such document");
//                    }
                    if (!exists) update_DB(db, data);
                    getData(db);
                } else {
                    Log.d("Firestore error", "get failed with ", user.getException());
                }
            }
        });
    }

    public void getData(FirebaseFirestore db) {

        balance = findViewById(R.id.bal);
        phno = findViewById(R.id.phno);
        pname = findViewById(R.id.proname);
        final DocumentSnapshot[] document = new DocumentSnapshot[1];
        Task<DocumentSnapshot> user = db.collection("users").document(phonenumber).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> user) {
                if (user.isSuccessful()) {
                    document[0] = user.getResult();
                    if (document[0].exists()) {
                        Log.d("Firestore", "Document retrieved");
                        phonenumber = String.valueOf(document[0].get("phone"));
                        name = String.valueOf(document[0].get("name"));
                        bal = String.valueOf(document[0].get("balance"));
                        prev_trans = (ArrayList) document[0].get("prev_transactions");
                        phno.setText(phonenumber);
                        pname.setText(name);
                        balance.setText("Balance:" + bal);
                    } else {
                        Log.d("Firestore", "No such document");
                    }
                } else {
                    Log.d("Firestore error", "get failed with ", user.getException());
                }
            }
        });
    }


    //Getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            Log.d("QRScan","Result obtained.Processing result.");
            if (result != null) {
                //if qr contains data
                try {
                    //converting the data to json
                    JSONObject obj = new JSONObject(result.getContents());
                    to_phonenumber = "+91" + obj.getString("phone");
                } catch (JSONException e) {
                    e.printStackTrace();
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                }
                if(phonenumber != null){
                    Intent i = new Intent(payment_options.this,PaymentActivity.class);
                    i.putExtra("to_phone",to_phonenumber);
                    i.putExtra("from_phone",phonenumber);
                    startActivity(i);
                }
            }
        } else {
            Toast.makeText(this,"Scan cancelled.",Toast.LENGTH_LONG).show();
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}