package com.example.mobilerehab;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.example.mobilerehab.SharedPref.SHARED_PREF_NAME;
import static com.example.mobilerehab.SharedPref.mCtx;

public class PatientProfile extends AppCompatActivity {

    final String updateUrl = "http://192.168.43.166/MobileRehab/patient.php";
    final Calendar calendar = Calendar.getInstance();
    EditText editText_patientname, editText_patienticnumber, editText_patientaddress, editText_patientphonenumber, editText_patientemail, editText_patientstartdate;
    TextView textView_userid;
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
        editText_patientemail = findViewById(R.id.editText_patientemail);
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
                updateAlert();
            }
        });

        textView_userid = findViewById(R.id.textView_userid);
        String user_id = SharedPref.getInstance(this).LoggedInUser();
        textView_userid.setText(user_id);

        populateProfile();
    }

    private void updateAlert() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Profile update");
        alertDialog.setMessage("Are you sure you want to continue?");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateProfile();
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertdialog = alertDialog.create();
        alertdialog.show();
    }

    private void populateProfile() {

        final String user_id = textView_userid.getText().toString();
        final String appointmentUrl = "http://192.168.43.166/MobileRehab/populateprofile.php?user_id=" + user_id;
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Retrieving Data, Please Wait");
        progressDialog.show();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(appointmentUrl, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        PatientData patientData = new PatientData();
                        patientData.setPatient_name(jsonObject.getString("patient_name"));
                        patientData.setPatient_icnumber(jsonObject.getString("patient_icnumber"));
                        patientData.setPatient_address(jsonObject.getString("patient_address"));
                        patientData.setPatient_phonenumber(jsonObject.getString("patient_phonenumber"));
                        patientData.setPatient_email(jsonObject.getString("patient_email"));
                        patientData.setPatient_startdate(jsonObject.getString("patient_startdate"));

                        editText_patientname.setText(patientData.getPatient_name());
                        editText_patienticnumber.setText(patientData.getPatient_icnumber());
                        editText_patientaddress.setText(patientData.getPatient_address());
                        editText_patientphonenumber.setText(patientData.getPatient_phonenumber());
                        editText_patientemail.setText(patientData.getPatient_email());
                        editText_patientstartdate.setText(patientData.getPatient_startdate());

                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }
                }
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                progressDialog.dismiss();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);

    }

    private void updateProfile() {

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        final String doctor_id = sharedPreferences.getString("createdby", "");
        final String user_id = textView_userid.getText().toString();
        final String patient_name = editText_patientname.getText().toString();
        final String patient_icnumber = editText_patienticnumber.getText().toString();
        final String patient_address = editText_patientaddress.getText().toString();
        final String patient_phonenumber = editText_patientphonenumber.getText().toString();
        final String patient_email = editText_patientemail.getText().toString();
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
                params.put("patient_email", patient_email);
                params.put("patient_startdate", patient_startdate);

                return params;
            }
        };
        VolleySingleton.getInstance(PatientProfile.this).addToRequestQueue(stringRequest);
        startActivity(new Intent(getApplicationContext(), PatientHome.class));
    }

}
