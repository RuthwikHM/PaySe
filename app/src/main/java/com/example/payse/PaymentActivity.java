package com.example.payse;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;


public class PaymentActivity extends AppCompatActivity {
    private Button pay;
    private String to_phonenumber,from_phonenumber;
    private TextView phone;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment);

        to_phonenumber = getIntent().getStringExtra("to_phone");
        from_phonenumber = getIntent().getStringExtra("from_phone");
        db = FirebaseFirestore.getInstance();

        phone = findViewById(R.id.to);
        phone.setText("To:" + to_phonenumber);

        pay = findViewById(R.id.pay);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText Amt = findViewById(R.id.Amt);
                final EditText pin = findViewById(R.id.pin);
                final String inp_pin = String.valueOf(pin.getText());
                final int Amount = Integer.parseInt(String.valueOf(Amt.getText()));
                final DocumentReference from = db.collection("users").document(from_phonenumber);
                final DocumentReference to = db.collection("users").document(to_phonenumber);

                db.runTransaction(new Transaction.Function<Void>() {

                    @Nullable
                    @Override
                    public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                        Looper.prepare();
                        DocumentSnapshot from_snapshot = transaction.get(from);
                        DocumentSnapshot to_snapshot = transaction.get(to);
                        int balance = Integer.parseInt(from_snapshot.getString("balance"));
                        String PIN = from_snapshot.getString("pin");
                        int to_balance = Integer.parseInt(to_snapshot.getString("balance"));
                        if(inp_pin.equals(PIN)){
                            if(Amount < balance){
                                 balance -= Amount;
                                 to_balance += Amount;
                                 transaction.update(from,"balance",balance);
                                 transaction.update(to,"balance",to_balance);
                                 Intent i = new Intent(PaymentActivity.this,payment_options.class);
                                 i.putExtra("phone",from_phonenumber);
//                                 Toast.makeText(PaymentActivity.this,"Transaction successful",Toast.LENGTH_LONG).show();
                                Log.d("Firestore","Transaction successful.");
                                 startActivity(i);
                                return null;
                            }
                            else{
                                Toast.makeText(PaymentActivity.this,"Amount cannot be greater than balance",Toast.LENGTH_LONG).show();
                                throw new FirebaseFirestoreException("Amount cannot be greater than balance", FirebaseFirestoreException.Code.ABORTED);
                            }
                        }
                        else{
                            Toast.makeText(PaymentActivity.this,"Invalid PIN.",Toast.LENGTH_LONG).show();
                            throw new FirebaseFirestoreException("Invalid PIN", FirebaseFirestoreException.Code.ABORTED);
                        }
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void result) {
                        Log.d("Firestore", "Transaction success");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("Firestore", "Transaction failure.", e);
                            }
                        });

//                db.collection("user").document(from_phonenumber).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull final Task<DocumentSnapshot> task) {
//                        final int balance = (int) from.get().getResult().get("balance");
//                        String PIN = String.valueOf(from.get().getResult().get("pin"));
//                        if(inp_pin.equals(PIN)){
//                            if(Amount < balance){
//                                db.runTransaction(new Transaction.Function<Void>() {
//                                    @Nullable
//                                    @Override
//                                    public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
//                                        DocumentReference to = db.collection("users").document(to_phonenumber);
//                                        int bal = balance;
//                                        int new_bal = (int) to.get().getResult().get("balance");
//                                        bal -= Amount;
//                                        new_bal += Amount;
//                                        transaction.update(from,"balance",balance);
//                                        transaction.update(to,"balance",new_bal);
//
//                                        return null;
//                                    }
//                                }).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        Toast.makeText(PaymentActivity.this, "Transaction successful", Toast.LENGTH_LONG).show();
//                                        Intent i = new Intent(PaymentActivity.this,payment_options.class);
//                                        startActivity(i);
//                                    }
//                                }).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void aVoid) {
//                                        Log.d("Firestore","Transaction completed.");
//                                    }
//                                }).addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        Log.w("Firestore", "Transaction failure.", e);
//                                        Toast.makeText(PaymentActivity.this, "Transaction failed", Toast.LENGTH_LONG).show();
//                                    }
//                                });
//                            }
//                        }
//                        else Toast.makeText(PaymentActivity.this,"Invalid PIN",Toast.LENGTH_LONG).show();
//                    }
//                });
            }
        });
    }

}
