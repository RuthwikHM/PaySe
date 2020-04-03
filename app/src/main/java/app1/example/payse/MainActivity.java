package app1.example.payse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private CardView getstarted;
    private EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getstarted = findViewById(R.id.loginCard);
        name = (EditText) findViewById(R.id.uname1);
        final String fullName = name.getText().toString();
        getstarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(MainActivity.this, payment_options.class);
                i1.putExtra("full_name",fullName);
                startActivity(i1);
            }
        });
    }

}
