package com.example.payse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PaymentActivity extends AppCompatActivity {
    private Button pay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        setListeners();
        pay = findViewById(R.id.pay);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText Pno = findViewById(R.id.Pno);
                EditText Amt = findViewById(R.id.Amt);
                String Phone = String.valueOf(Pno.getText());
                String Amount = String.valueOf(Amt.getText());
                if(Phone.length() == 10) {
                    if(Amt.length() != 0 && Integer.parseInt(Amount) > 0) {
                        Intent i = new Intent(PaymentActivity.this, MainActivity.class);
                        startActivity(i);
                    }
                    else Toast.makeText(PaymentActivity.this,R.string.amt_err,Toast.LENGTH_LONG).show();
                }
                else Toast.makeText(PaymentActivity.this,"Enter a valid phone number",Toast.LENGTH_LONG).show();
            }
        });
    }

    void setListeners(){

    }
}
