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
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment);
//        setListeners();
        pay = findViewById(R.id.pay);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText Amt = findViewById(R.id.Amt);
                String Amount = String.valueOf(Amt.getText());
                if(Amt.length() != 0 && Integer.parseInt(Amount) > 0) {
                    Intent i = new Intent(PaymentActivity.this, MainActivity.class);
                    i.putExtra("Status",1);
                    i.putExtra("user",savedInstanceState.get("user").toString());
                    startActivity(i);
                }
                else Toast.makeText(PaymentActivity.this,R.string.amt_err,Toast.LENGTH_LONG).show();
            }});
        }

    void setListeners(){

    }
}
