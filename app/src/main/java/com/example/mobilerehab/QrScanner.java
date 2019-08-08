package com.example.mobilerehab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class QrScanner extends AppCompatActivity {

    TextView textView_doctorid, textView_appointmentid, textView_scandoctorid;
    Button button_startscan;
    String attendanceUrl = "http://192.168.43.166/MobileRehab/attendance.php";
    private IntentIntegrator qrScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scanner);

        textView_doctorid = findViewById(R.id.textView_doctorid);
        textView_appointmentid = findViewById(R.id.textView_appointmentid);
        textView_scandoctorid = findViewById(R.id.textView_scandoctorid);

        String doctor_id = getIntent().getExtras().getString("extra_doctorid");
        textView_doctorid.setText(doctor_id);

        String appointment_id = getIntent().getExtras().getString("extra_appointmentid");
        textView_appointmentid.setText(appointment_id);

        qrScan = new IntentIntegrator(this);

        button_startscan = findViewById(R.id.button_startscan);
        button_startscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qrScan.initiateScan();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                try {
                    JSONObject jsonObject = new JSONObject(result.getContents());
                    textView_scandoctorid.setText(jsonObject.getString("doctor_id"));
                    validateAttendance();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    protected void validateAttendance() {

        final String doctor_id = textView_doctorid.getText().toString();
        final String scandoctor_id = textView_scandoctorid.getText().toString();

        if (!doctor_id.equals(scandoctor_id)) {
            Toast.makeText(getApplicationContext(), "Not Valid", Toast.LENGTH_SHORT).show();
        } else {
            updateAttendance();
        }
    }

    protected void updateAttendance() {

        final String appointment_id = textView_appointmentid.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, attendanceUrl,
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
                        Toast.makeText(getApplicationContext(), "Connection Error" + error, Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("appointment_id", appointment_id);

                return params;
            }
        };
        VolleySingleton.getInstance(QrScanner.this).addToRequestQueue(stringRequest);

    }
}
