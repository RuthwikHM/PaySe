package com.example.payse;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity{
    private Button scan,receive,signup,login;
    private IntentIntegrator qrScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        scan = findViewById(R.id.buttonScan);
        receive = findViewById(R.id.buttonReceive);
        signup = findViewById(R.id.signupnav);
        login = findViewById(R.id.loginnav);
        qrScan = new IntentIntegrator(this);

        scan.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                qrScan.initiateScan();
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,SignUpActivity.class);
                startActivity(i);
                Log.d("Activity","SignupActivity");
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(i);
                Log.d("Activity","LoginActivity");
            }
        });

        receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone_number = "8431144466";
                String qrcode;
                qrcode = "https://api.qrserver.com/v1/create-qr-code/?size=300x300&data=" + phone_number ;
                ImageView qr = findViewById(R.id.qr);
                new DownloadImageTask(qr).execute(qrcode);
            }
        });
    }

    //Getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Invalid QR code.Try again.", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {
                    //converting the data to json
                    JSONObject obj = new JSONObject(result.getContents());
//                    Toast.makeText(this, obj.toString(), Toast.LENGTH_LONG).show();
                    Intent i = new Intent(this,PaymentActivity.class);
                    i.putExtra("user",result.getContents());
                    startActivity(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            Toast.makeText(this,"Transaction failed.Try again.",Toast.LENGTH_LONG);
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openConnection().getInputStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }




}

//public class MainActivity extends AppCompatActivity implements View.OnClickListener {
//
//    //View Objects
//    private Button buttonScan;
//    private TextView textViewName, textViewAddress;
//
//    //qr code scanner object
//    private IntentIntegrator qrScan;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main);
//
//        //View objects
//        buttonScan = (Button) findViewById(R.id.buttonScan);
//        textViewName = (TextView) findViewById(R.id.textViewName);
//        textViewAddress = (TextView) findViewById(R.id.textViewAddress);
//
//        //intializing scan object
//        qrScan = new IntentIntegrator(this);
//
//        //attaching onclick listener
//        buttonScan.setOnClickListener(this);
//    }
//
//    //Getting the scan results
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//        if (result != null) {
//            //if qrcode has nothing in it
//            if (result.getContents() == null) {
//                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
//            } else {
//                //if qr contains data
//                try {
//                    //converting the data to json
//                    JSONObject obj = new JSONObject(result.getContents());
//                    //setting values to textviews
//                    textViewName.setText(obj.getString("name"));
//                    textViewAddress.setText(obj.getString("amount"));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    //if control comes here
//                    //that means the encoded format not matches
//                    //in this case you can display whatever data is available on the qrcode
//                    //to a toast
//                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
//                }
//            }
//        } else {
//            super.onActivityResult(requestCode, resultCode, data);
//        }
//    }
//    @Override
//    public void onClick(View view) {
//        //initiating the qr code scan
//        qrScan.initiateScan();
//    }
//}