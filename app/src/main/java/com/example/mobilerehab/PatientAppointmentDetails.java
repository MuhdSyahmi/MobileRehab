package com.example.mobilerehab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PatientAppointmentDetails extends AppCompatActivity {

    TextView textView_userid, textView_appointmentid, textView_doctorid, textView_patientname, textView_appointmenttime, textView_appointmentdate;
    Button button_scanqr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_appointment_details);

        textView_userid = findViewById(R.id.textView_userid);
        textView_appointmentid = findViewById(R.id.textView_appointmentid);
        textView_doctorid = findViewById(R.id.textView_doctorid);
        textView_patientname = findViewById(R.id.textView_patient_name);
        textView_appointmenttime = findViewById(R.id.textView_appointmenttime);
        textView_appointmentdate = findViewById(R.id.textView_appointmentdate);

        String user_id = getIntent().getExtras().getString("extra_userid");
        textView_userid.setText(user_id);
        final String appointmentid = getIntent().getExtras().getString("extra_appointmentid");
        textView_appointmentid.setText(appointmentid);
        final String doctorid = getIntent().getExtras().getString("extra_doctorid");
        textView_doctorid.setText(doctorid);
        String patientname = getIntent().getExtras().getString("extra_patientname");
        textView_patientname.setText(patientname);
        String appointmenttime = getIntent().getExtras().getString("extra_appointmenttime");
        textView_appointmenttime.setText(appointmenttime);
        String appointmentdate = getIntent().getExtras().getString("extra_appointmentdate");
        textView_appointmentdate.setText(appointmentdate);

        button_scanqr = findViewById(R.id.button_scanqr);
        button_scanqr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), QrScanner.class);
                intent.putExtra("extra_doctorid", doctorid);
                intent.putExtra("extra_appointmentid", appointmentid);
                startActivity(intent);
            }
        });
    }

}
