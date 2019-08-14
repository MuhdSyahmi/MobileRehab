package com.example.mobilerehab;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.R.layout.simple_spinner_item;

public class AddAppointment extends AppCompatActivity {

    final String appointmentUrl = "http://192.168.0.121/MobileRehab/appointment.php";

    EditText editText_patientid, editText_patientname, editText_patientemail, editText_appointmentdate, editText_appointmenttime;
    Button button_addappointment;
    TextView textView_userid;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    ArrayList<SpinnerData> spinnerDataArrayList = new ArrayList<>();
    ArrayList<String> patient_name = new ArrayList<>();
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addappointment);

        editText_patientid = findViewById(R.id.editText_patientid);
        editText_patientname = findViewById(R.id.editText_patientname);
        editText_patientemail = findViewById(R.id.editText_patientemail);
        editText_appointmentdate = findViewById(R.id.editText_appointmentdate);
        editText_appointmentdate.setInputType(InputType.TYPE_NULL);
        editText_appointmentdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(AddAppointment.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        editText_appointmentdate.setText(year + "/" + (month + 1) + "/" + dayOfMonth);
                    }
                }, year, month, day);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });
        editText_appointmenttime = findViewById(R.id.editText_appointmenttime);
        editText_appointmenttime.setInputType(InputType.TYPE_NULL);
        editText_appointmenttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                timePickerDialog = new TimePickerDialog(AddAppointment.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        editText_appointmenttime.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);
                timePickerDialog.setTitle("Select Time");
                timePickerDialog.show();
            }
        });

        button_addappointment = findViewById(R.id.button_addappointment);

        button_addappointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAppointment();
                Intent intent = new Intent(Intent.ACTION_SEND);
            }
        });

        textView_userid = findViewById(R.id.textView_userid);
        String user_id = SharedPref.getInstance(this).LoggedInUser();
        textView_userid = findViewById(R.id.textView_userid);
        textView_userid.setText(user_id);

        populateSpinner();
        spinner = findViewById(R.id.spinner_patientname);
        patient_name = new ArrayList<>();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerData spinnerData = spinnerDataArrayList.get(position);
                editText_patientid.setText(Integer.toString(spinnerData.getPatient_id()));
                editText_patientname.setText(spinnerData.getPatient_name());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                editText_patientid.setText("");
                editText_patientname.setText("");
            }
        });
    }

    private void populateSpinner() {
        final String user_id = textView_userid.getText().toString();
        final String spinnerUrl = "http://192.168.0.121/MobileRehab/appointmentspinner.php?appointment_doctorid=" + user_id;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(spinnerUrl, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        SpinnerData spinnerData = new SpinnerData();
                        spinnerData.setPatient_id(jsonObject.getInt("user_id"));
                        spinnerData.setPatient_name(jsonObject.getString("patient_name"));
                        spinnerDataArrayList.add(spinnerData);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                for (int i = 0; i < spinnerDataArrayList.size(); i++) {
                    patient_name.add(spinnerDataArrayList.get(i).getPatient_name());
                }
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(AddAppointment.this, simple_spinner_item, patient_name);
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(spinnerArrayAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    private void addAppointment() {

        final String appointment_doctorid = SharedPref.getInstance(this).LoggedInUser();
        final String appointment_patientid = editText_patientid.getText().toString();
        final String appointment_date = editText_appointmentdate.getText().toString();
        final String appointment_time = editText_appointmenttime.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, appointmentUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                            JSONObject obj = new JSONObject(response);
                            if (obj.getBoolean("error")) {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            } else {

                                startActivity(new Intent(getApplicationContext(), AddAppointment.class));

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Connection Error" + error, Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("selectFn", "addappointment");
                params.put("appointment_doctorid", appointment_doctorid);
                params.put("appointment_patientid", appointment_patientid);
                params.put("appointment_date", appointment_date);
                params.put("appointment_time", appointment_time);

                return params;
            }
        };
        VolleySingleton.getInstance(AddAppointment.this).addToRequestQueue(stringRequest);
        startActivity(new Intent(getApplicationContext(), DoctorHome.class));
    }
}

