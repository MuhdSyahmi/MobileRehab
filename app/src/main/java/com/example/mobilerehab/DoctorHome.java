package com.example.mobilerehab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class DoctorHome extends AppCompatActivity {

    Button button_addpatient, button_addappointment, button_viewappointment, button_viewpatient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_home);

        button_addpatient = findViewById(R.id.button_addpatient);
        button_addpatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddPatient.class);
                startActivity(intent);
            }
        });

        button_viewpatient = findViewById(R.id.button_viewpatient);
        button_viewpatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ViewPatient.class);
                startActivity(intent);
            }
        });

        button_addappointment = findViewById(R.id.button_addappointment);
        button_addappointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), AddAppointment.class);
                startActivity(intent1);
            }
        });

        button_viewappointment = findViewById(R.id.button_viewappointment);
        button_viewappointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getApplicationContext(), ViewAppointment.class);
                startActivity(intent2);
            }
        });
    }
}
