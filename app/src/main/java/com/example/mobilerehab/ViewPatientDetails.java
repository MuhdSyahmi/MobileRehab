package com.example.mobilerehab;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class ViewPatientDetails extends AppCompatActivity {

    TextView textView_patientname, textView_patienticnumber, textView_patientaddress, textView_patientphonenumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_patient_details);

        textView_patientname = findViewById(R.id.textView_patientname);
        textView_patienticnumber = findViewById(R.id.textView_patienticnumber);
        textView_patientaddress = findViewById(R.id.textView_patientaddress);
        textView_patientphonenumber = findViewById(R.id.textView_patientphonenumber);

        String patientname = getIntent().getExtras().getString("extra_patientname");
        textView_patientname.setText(patientname);
        String patienticnumber = getIntent().getExtras().getString("extra_patienticnumber");
        textView_patienticnumber.setText(patienticnumber);
        String patientaddress = getIntent().getExtras().getString("extra_patientaddress");
        textView_patientaddress.setText(patientaddress);
        String patientphonenumber = getIntent().getExtras().getString("extra_patientphonenumber");
        textView_patientphonenumber.setText(patientphonenumber);
    }
}
