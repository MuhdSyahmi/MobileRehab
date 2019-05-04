package com.example.mobilerehab;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.TimePicker;
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

public class CreateAppointment extends AppCompatActivity {

    final String appointmentUrl = "http://192.168.1.33/MobileRehab/appointment.php";
    EditText editText_patientname, editText_appointmentdate, editText_appointmenttime;
    Button button_addappointment;
    TextView textView_userid;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createappointment);

        editText_patientname = findViewById(R.id.editText_patientname);
        editText_appointmentdate = findViewById(R.id.editText_appointmentdate);
        editText_appointmentdate.setInputType(InputType.TYPE_NULL);
        editText_appointmentdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                AppointmentClient.setAlarmForNotification(calendar);

                datePickerDialog = new DatePickerDialog(CreateAppointment.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        editText_appointmentdate.setText(year + "/" + (month + 1) + "/" + dayOfMonth);
                    }
                }, year, month, day);
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
                timePickerDialog = new TimePickerDialog(CreateAppointment.this, new TimePickerDialog.OnTimeSetListener() {
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
            }
        });

        textView_userid = findViewById(R.id.textView_userid);
        SharedPreferences sharedPreferences = getSharedPreferences("SharedPref", Context.MODE_PRIVATE);
        String doctor_id = sharedPreferences.getString("user_id", "");
        textView_userid = findViewById(R.id.textView_userid);
        textView_userid.setText(doctor_id);
    }

    private void addAppointment() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        final String appointment_doctorid = sharedPreferences.getString("user_id", "");
        final String appointment_patientname = editText_patientname.getText().toString();
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

                                startActivity(new Intent(getApplicationContext(), CreateAppointment.class));

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
                params.put("appointment_patientname", appointment_patientname);
                params.put("appointment_date", appointment_date);
                params.put("appointment_time", appointment_time);

                return params;
            }
        };
        VolleySingleton.getInstance(CreateAppointment.this).addToRequestQueue(stringRequest);
    }
}

