package com.example.mobilerehab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class CreateAppointment extends AppCompatActivity {

    EditText editText_patientname, editText_appointmentdate, editText_appointmenttime;
    Button button_addappointment;

    final String appointmentUrl = "http://192.168.1.13/MobileRehab/appointment.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createappointment);

        editText_patientname = (EditText) findViewById(R.id.editText_patientname);
        editText_appointmentdate = (EditText) findViewById(R.id.editText_appointmentdate);
        editText_appointmenttime = (EditText) findViewById(R.id.editText_appointmenttime);
        button_addappointment = (Button) findViewById(R.id.button_addappointment);

        button_addappointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAppointment();
            }
        });
    }

    private void addAppointment() {
        final String app_patientname = editText_patientname.getText().toString();
        final String app_date = editText_appointmentdate.getText().toString();
        final String app_time = editText_appointmenttime.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, appointmentUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getApplicationContext(), "Connection Error" + error, Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("selectFn", "addappointment");
                params.put("appointment_patientname", app_patientname);
                params.put("appointment_date", app_date);
                params.put("appointment_time", app_time);

                return params;
            }
        };
        VolleySingleton.getInstance(CreateAppointment.this).addToRequestQueue(stringRequest);
    }
}

