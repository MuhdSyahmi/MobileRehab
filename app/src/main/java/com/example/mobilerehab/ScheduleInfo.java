package com.example.mobilerehab;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import java.util.HashMap;
import java.util.Map;

import static com.example.mobilerehab.Schedule.extra_patientname;
import static com.example.mobilerehab.Schedule.extra_scheduledate;
import static com.example.mobilerehab.Schedule.extra_scheduleid;
import static com.example.mobilerehab.Schedule.extra_scheduletime;

public class ScheduleInfo extends AppCompatActivity {

    private EditText textView_scheduledate, textView_scheduletime;
    private TextView textView_scheduleid, textView_patientname;
    private Button button_update, button_delete;

    final String appointmentUrl = "http://192.168.1.13/MobileRehab/appointment.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_info);

        Intent intent = getIntent();

        String scheduleid = intent.getStringExtra(extra_scheduleid);
        String patientname = intent.getStringExtra(extra_patientname);
        String scheduledate = intent.getStringExtra(extra_scheduledate);
        String scheduletime = intent.getStringExtra(extra_scheduletime);

        textView_scheduleid = (TextView) findViewById(R.id.textView_scheduleid);
        textView_patientname = (TextView) findViewById(R.id.textView_patient_name);
        textView_scheduledate = (EditText) findViewById(R.id.textView_scheduledate);
        textView_scheduletime = (EditText) findViewById(R.id.textView_scheduletime);

        button_update = (Button) findViewById(R.id.button_updateschedule);
        button_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSchedule();
            }
        });

        button_delete = (Button) findViewById(R.id.button_deleteschedule);
        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSchedule();
            }
        });

        textView_scheduleid.setText(scheduleid);
        textView_patientname.setText(patientname);
        textView_scheduledate.setText(scheduledate);
        textView_scheduletime.setText(scheduletime);
    }

    private void updateSchedule(){
        final String schedule_id = textView_scheduleid.getText().toString();
        final String schedule_patientname = textView_patientname.getText().toString();
        final String schedule_date = textView_scheduledate.getText().toString();
        final String schedule_time = textView_scheduletime.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,appointmentUrl,
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
                params.put("selectFn","updateappointment");
                params.put("schedule_id",schedule_id);
                params.put("schedule_patientname",schedule_patientname);
                params.put("schedule_date",schedule_date);
                params.put("schedule_time",schedule_time);

                return params;
            }
        };
        VolleySingleton.getInstance(ScheduleInfo.this).addToRequestQueue(stringRequest);
    }

    private void deleteSchedule(){

        final String schedule_id = textView_scheduleid.getText().toString();
        final String schedule_patientname = textView_patientname.getText().toString();
        final String schedule_date = textView_scheduledate.getText().toString();
        final String schedule_time = textView_scheduletime.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,appointmentUrl,
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
                params.put("selectFn","deleteappointment");
                params.put("schedule_id",schedule_id);
                params.put("schedule_patientname",schedule_patientname);
                params.put("schedule_date",schedule_date);
                params.put("schedule_time",schedule_time);

                return params;
            }
        };
        VolleySingleton.getInstance(ScheduleInfo.this).addToRequestQueue(stringRequest);
    }
}
