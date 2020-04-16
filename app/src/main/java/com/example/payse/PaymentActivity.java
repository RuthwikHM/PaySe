package com.example.payse;

import android.annotation.SuppressLint;
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
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.Collections;


public class PaymentActivity extends AppCompatActivity {
    private CardView confirm;
    private String to_phonenumber,from_phonenumber;
    private TextView phone;
    private FirebaseFirestore db;
    private ArrayList from_prev_transactions,to_prev_transactions;
    int flag = -1;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment);

        to_phonenumber = getIntent().getStringExtra("to_phone");
        from_phonenumber = getIntent().getStringExtra("from_phone");
        db = FirebaseFirestore.getInstance();

        phone = findViewById(R.id.to);
        phone.setText("To:" + to_phonenumber);

        confirm =  findViewById(R.id.confirmCard);
        confirm.setOnClickListener(new View.OnClickListener() {
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
                        int balance = Integer.parseInt(String.valueOf(from_snapshot.get("balance")));
                        from_prev_transactions = (ArrayList) from_snapshot.get("prev_transactions");
                        to_prev_transactions = (ArrayList) to_snapshot.get("prev_transactions");
                        String PIN = String.valueOf(from_snapshot.get("pin"));
                        int to_balance = Integer.parseInt(String.valueOf(to_snapshot.get("balance")));
                        if(inp_pin.equals(PIN)){
                            if(Amount < balance){
                                 balance -= Amount;
                                 to_balance += Amount;
                                Collections.rotate(from_prev_transactions,1);
                                Collections.rotate(to_prev_transactions,1);
                                from_prev_transactions.set(0,-Amount);
                                to_prev_transactions.set(0,Amount);
                                 transaction.update(from,"balance",balance);
                                 transaction.update(from,"prev_transactions",from_prev_transactions);
                                 transaction.update(to,"balance",to_balance);
                                 transaction.update(to,"prev_transactions",to_prev_transactions);
                                 flag = 0;
                                 Intent i = new Intent(PaymentActivity.this,payment_options.class);
                                 i.putExtra("phone",from_phonenumber);
                                 i.putExtra("transaction_status",flag);
                                 Log.d("Firestore","Transaction successful.");
                                 startActivity(i);
                                 finish();
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
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}
