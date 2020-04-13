package com.example.payse;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class recent_payments extends AppCompatActivity {

    private ArrayList prev_transactions;
    private TextView recent_transactions[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_payments);
        long ele;
        prev_transactions = getIntent().getIntegerArrayListExtra("prev_transactions");
        recent_transactions = new TextView[10];
        recent_transactions[0] = findViewById(R.id.transaction1);
        recent_transactions[1] = findViewById(R.id.transaction2);
        recent_transactions[2] = findViewById(R.id.transaction3);
        recent_transactions[3] = findViewById(R.id.transaction4);
        recent_transactions[4] = findViewById(R.id.transaction5);
        recent_transactions[5] = findViewById(R.id.transaction6);
        recent_transactions[6] = findViewById(R.id.transaction7);
        recent_transactions[7] = findViewById(R.id.transaction8);
        recent_transactions[8] = findViewById(R.id.transaction9);
        recent_transactions[9] = findViewById(R.id.transaction10);
        for(int i = 0;i < 10;i++){
            ele = (long) prev_transactions.get(i);
            if(ele > 0){
                recent_transactions[i].setText("+ Rs." + ele);
                recent_transactions[i].setTextColor(getColor(R.color.green));
            }
            else if(ele < 0){
                recent_transactions[i].setText("- Rs." + -ele);
                recent_transactions[i].setTextColor(getColor(R.color.red));
            }
            else recent_transactions[i].setText("Rs." + ele);
        }
    }
}
