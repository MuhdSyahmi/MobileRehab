package com.example.mobilerehab;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

public class AppointmentDetails extends AppCompatActivity {

    final String appointmentUrl = "http://192.168.43.166/MobileRehab/appointment.php";
    EditText editText_appointmentdate, editText_appointmenttime;
    TextView textView_appointmentid, textView_patientname, textView_appointmentstatus;
    Button button_update, button_delete;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_details);

        textView_appointmentid = findViewById(R.id.textView_appointmentid);
        textView_patientname = findViewById(R.id.textView_patientname);
        editText_appointmentdate = findViewById(R.id.editText_appointmentdate);
        editText_appointmenttime = findViewById(R.id.editText_appointmenttime);
        textView_appointmentstatus = findViewById(R.id.textView_appointmentstatus);

        String appointmentid = getIntent().getExtras().getString("extra_appointmentid");
        textView_appointmentid.setText(appointmentid);
        String patientname = getIntent().getExtras().getString("extra_patientname");
        textView_patientname.setText(patientname);
        String appointmentdate = getIntent().getExtras().getString("extra_appointmentdate");
        editText_appointmentdate.setText(appointmentdate);
        String appointmenttime = getIntent().getExtras().getString("extra_appointmenttime");
        editText_appointmenttime.setText(appointmenttime);
        String appointment_status = getIntent().getExtras().getString("extra_appointmentstatus");
        textView_appointmentstatus.setText(appointment_status);

        editText_appointmenttime.setInputType(InputType.TYPE_NULL);
        editText_appointmenttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                timePickerDialog = new TimePickerDialog(AppointmentDetails.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        editText_appointmenttime.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);
                timePickerDialog.setTitle("Select Time");
                timePickerDialog.show();
            }
        });

        editText_appointmentdate.setInputType(InputType.TYPE_NULL);
        editText_appointmentdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(AppointmentDetails.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        editText_appointmentdate.setText(year + "/" + (month + 1) + "/" + dayOfMonth);
                    }
                }, year, month, day);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        button_update = findViewById(R.id.button_updateappointment);
        button_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAlert();
            }
        });

        button_delete = findViewById(R.id.button_deleteappointment);
        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAlert();
            }
        });
    }

    private void updateAlert() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Update Appointment");
        alertDialog.setMessage("Are you sure you want to update?");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateAppointment();
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

    private void deleteAlert() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Delete appointment");
        alertDialog.setMessage("Are you sure you want to delete?");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteAppointment();
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

    private void updateAppointment() {
        final String appointment_id = textView_appointmentid.getText().toString();
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
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("selectFn", "updateappointment");
                params.put("appointment_id", appointment_id);
                params.put("appointment_date", appointment_date);
                params.put("appointment_time", appointment_time);

                return params;
            }
        };
        VolleySingleton.getInstance(AppointmentDetails.this).addToRequestQueue(stringRequest);
        startActivity(new Intent(getApplicationContext(), ViewAppointment.class));
    }

    private void deleteAppointment() {

        final String appointment_id = textView_appointmentid.getText().toString();

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
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("selectFn", "deleteappointment");
                params.put("appointment_id", appointment_id);

                return params;
            }
        };
        VolleySingleton.getInstance(AppointmentDetails.this).addToRequestQueue(stringRequest);
        startActivity(new Intent(getApplicationContext(), ViewAppointment.class));
    }
}
