package com.example.mobilerehab;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class EmergencyContacts extends AppCompatActivity {

    Button button_callpolice, button_callhospital;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contacts);

        button_callpolice = findViewById(R.id.button_callpolice);
        button_callpolice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String police_number = "999";
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + police_number));
                startActivity(callIntent);
            }
        });

        button_callhospital = findViewById(R.id.button_callhospital);
        button_callhospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hospital_number = "999";
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + hospital_number));
                startActivity(callIntent);
            }
        });


    }
}
