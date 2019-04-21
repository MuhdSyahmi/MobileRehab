package com.example.mobilerehab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class PatientHome extends AppCompatActivity {

    Button button_patientviewlocation;
    Button button_patientscanqr;
    Button button_patientviewprofile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_home);

        button_patientviewprofile = (Button) findViewById(R.id.button_patientviewprofile);
        button_patientviewprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),PatientProfile.class);
                startActivity(intent);
            }
        });

        button_patientviewlocation = (Button) findViewById(R.id.button_patientviewlocation);
        button_patientviewlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
                startActivity(intent);
            }
        });

        button_patientscanqr = (Button) findViewById(R.id.button_patientscanqr);
        button_patientscanqr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),QrScanner.class);
                startActivity(intent);
            }
        });
    }
}
