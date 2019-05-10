package com.example.mobilerehab;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.example.mobilerehab.SharedPref.SHARED_PREF_NAME;
import static com.example.mobilerehab.SharedPref.mCtx;

public class PatientProfile extends AppCompatActivity {

    final String updateUrl = "http://10.131.73.39/MobileRehab/patient.php";
    final Calendar calendar = Calendar.getInstance();
    EditText editText_patientname, editText_patienticnumber, editText_patientaddress, editText_patientphonenumber, editText_patientstartdate;
    TextView textView_user_id;
    Button button_patientsave;
    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile);

        editText_patientname = findViewById(R.id.editText_patientname);
        editText_patienticnumber = findViewById(R.id.editText_patienticnumber);
        editText_patientaddress = findViewById(R.id.editText_patientaddress);
        editText_patientphonenumber = findViewById(R.id.editText_patientphonenumber);
        editText_patientstartdate = findViewById(R.id.editText_patientstartdate);

        editText_patientstartdate.setInputType(InputType.TYPE_NULL);
        editText_patientstartdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(PatientProfile.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        editText_patientstartdate.setText(year + "/" + (month + 1) + "/" + dayOfMonth);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        button_patientsave = findViewById(R.id.button_patientsave);
        button_patientsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });
    }

    private void updateProfile() {

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        final String user_id = sharedPreferences.getString("user_id", "");
        final String doctor_id = sharedPreferences.getString("createdby", "");
        final String patient_name = editText_patientname.getText().toString();
        final String patient_icnumber = editText_patienticnumber.getText().toString();
        final String patient_address = editText_patientaddress.getText().toString();
        final String patient_phonenumber = editText_patientphonenumber.getText().toString();
        final String patient_startdate = editText_patientstartdate.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,updateUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                            JSONObject obj = new JSONObject(response);
                            if (obj.getBoolean("error")) {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            } else {

                                String UserID = obj.getString("user_id");
                                SharedPref.getInstance(getApplicationContext()).storeUserName(UserID);

                                finish();

                                startActivity(new Intent(getApplicationContext(), PatientProfile.class));

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

                params.put("user_id", user_id);
                params.put("doctor_id", doctor_id);
                params.put("patient_name", patient_name);
                params.put("patient_icnumber", patient_icnumber);
                params.put("patient_address", patient_address);
                params.put("patient_phonenumber", patient_phonenumber);
                params.put("patient_startdate", patient_startdate);

                return params;
            }
        };
        VolleySingleton.getInstance(PatientProfile.this).addToRequestQueue(stringRequest);

    }

}
