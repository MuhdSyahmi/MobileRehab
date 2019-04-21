package com.example.mobilerehab;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PatientProfile extends AppCompatActivity {

    EditText editText_patientname, editText_patienticnumber, editText_patientaddress, editText_patientphonenumber, editText_patientstartdate;
    Button button_patientsave;

    final String updateUrl = "http://192.168.1.13/MobileRehab/patient.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile);

        editText_patientname = (EditText) findViewById(R.id.editText_patientname);
        editText_patienticnumber = (EditText) findViewById(R.id.editText_patienticnumber);
        editText_patientaddress = (EditText) findViewById(R.id.editText_patientaddress);
        editText_patientphonenumber = (EditText) findViewById(R.id.editText_patientphonenumber);
        editText_patientstartdate = (EditText) findViewById(R.id.editText_patientstartdate);

        button_patientsave = (Button) findViewById(R.id.button_patientsave);
        button_patientsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });
    }

    final Calendar calendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            updateLabel();
        }
    };

    private void updateLabel() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editText_patientstartdate.setText(sdf.format(calendar.getTime()));
    }

    private void updateProfile(){

        final String fullname = editText_patientname.getText().toString();
        final String icnumber = editText_patienticnumber.getText().toString();
        final String address = editText_patientaddress.getText().toString();
        final String phonenumber = editText_patientphonenumber.getText().toString();
        final String startdate = editText_patientstartdate.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,updateUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Toast.makeText(getApplicationContext(),response.toString(), Toast.LENGTH_SHORT).show();
                            JSONObject obj = new JSONObject(response);
                            if (obj.getBoolean("error")) {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            } else {

                                startActivity(new Intent(getApplicationContext(), DoctorHome.class));

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Connection Error"+error, Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("fullname",fullname);
                params.put("icnumber",icnumber);
                params.put("address",address);
                params.put("phonenumber",phonenumber);
                params.put("startdate",startdate);

                return params;
            }
        };
        VolleySingleton.getInstance(PatientProfile.this).addToRequestQueue(stringRequest);

    }

}
