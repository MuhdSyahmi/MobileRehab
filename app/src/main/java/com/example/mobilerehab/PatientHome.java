package com.example.mobilerehab;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class PatientHome extends AppCompatActivity {

    Button button_patientviewlocation;
    Button button_patientscanqr;
    Button button_patientviewappointment;
    Button button_patientviewprofile;
    Button button_call;
    Button button_logout;
    TextView textView_randomquotes, textView_startdate, textView_datecounter, textView_userid;

    String quotes, startdate;

    String quotesUrl = "http://10.131.73.39/MobileRehab/quotes.php";

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_home);

        button_patientviewprofile = findViewById(R.id.button_patientviewprofile);
        button_patientviewprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),PatientProfile.class);
                startActivity(intent);
            }
        });

        button_patientviewlocation = findViewById(R.id.button_patientviewlocation);
        button_patientviewlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
                startActivity(intent);
            }
        });

        button_patientviewappointment = findViewById(R.id.button_patientviewappointment);
        button_patientviewappointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PatientViewAppointment.class);
                startActivity(intent);
            }
        });

        button_patientscanqr = findViewById(R.id.button_patientscanqr);
        button_patientscanqr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),QrScanner.class);
                startActivity(intent);
            }
        });

        button_call = findViewById(R.id.button_call);
        button_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EmergencyContacts.class);
                startActivity(intent);
            }
        });

        button_logout = findViewById(R.id.button_logout);
        button_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPref.getInstance(getApplicationContext()).logout();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
        });

        textView_randomquotes = findViewById(R.id.textView_randomquotes);
        getRandomQuotes();

        String user_id = SharedPref.getInstance(this).LoggedInUser();
        textView_userid = findViewById(R.id.textView_userid);
        textView_userid.setText(user_id);

        textView_startdate = findViewById(R.id.textView_startdate);
        getStartDate();

        textView_datecounter = findViewById(R.id.textView_datecounter);

        countDownStart();

    }

    private void getRandomQuotes() {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(quotesUrl, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        Random random = new Random();
                        int index = random.nextInt(response.length());
                        JSONObject jsonObject = response.getJSONObject(index);
                        quotes = jsonObject.getString("quotes_str");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    textView_randomquotes.setText(quotes);

                }
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

    private void getStartDate() {

        final String user_id = textView_userid.getText().toString();

        String datecounterUrl = "http://10.131.73.39/MobileRehab/datecounter.php?user_id=" + user_id;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(datecounterUrl, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        startdate = jsonObject.getString("patient_startdate");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    textView_startdate.setText(startdate);
                }
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

    public void countDownStart() {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 1000);
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                            "yyyy/MM/dd");
                    Date futureDate = dateFormat.parse(startdate);
                    Date currentDate = new Date();
                    if (currentDate.after(futureDate)) {
                        long diff = currentDate.getTime() - futureDate.getTime();
                        long days = diff / (24 * 60 * 60 * 1000);
                        diff -= days * (24 * 60 * 60 * 1000);
                        textView_datecounter.setText("" + String.format("%02d", days));
                    } else {
                        textView_datecounter.setText("No Date");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 1 * 1000);
    }


}
