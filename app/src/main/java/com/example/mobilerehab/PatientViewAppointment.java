package com.example.mobilerehab;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PatientViewAppointment extends AppCompatActivity implements PatientViewAppointmentAdapter.OnPatientAppointmentListener {

    private TextView textView_userid;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private List<PatientViewAppointmentData> patientViewAppointmentDataList;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_view_appointment);

        recyclerView = findViewById(R.id.RecylerView_patientviewappointment);
        patientViewAppointmentDataList = new ArrayList<>();
        adapter = new PatientViewAppointmentAdapter(getApplicationContext(), patientViewAppointmentDataList, this);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);

        textView_userid = findViewById(R.id.textView_userid);
        String user_id = SharedPref.getInstance(this).LoggedInUser();
        textView_userid.setText(user_id);
        Toast.makeText(getApplicationContext(), user_id, Toast.LENGTH_SHORT);

        getAppointmentList();
    }

    private void getAppointmentList() {

        final String user_id = textView_userid.getText().toString();
        final String appointmentUrl = "http://192.168.1.48/MobileRehab/appointment.php?selectFn=patientviewappointment&user_id=" + user_id;
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Retrieving Data, Please Wait");
        progressDialog.show();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(appointmentUrl, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        PatientViewAppointmentData patientViewAppointmentData = new PatientViewAppointmentData();
                        patientViewAppointmentData.setUser_id(jsonObject.getInt("user_id"));
                        patientViewAppointmentData.setAppointment_id(jsonObject.getInt("appointment_id"));
                        patientViewAppointmentData.setDoctor_id(jsonObject.getInt("doctor_id"));
                        patientViewAppointmentData.setPatient_name(jsonObject.getString("patient_name"));
                        patientViewAppointmentData.setAppointment_time(jsonObject.getString("appointment_time"));
                        patientViewAppointmentData.setAppointment_date(jsonObject.getString("appointment_date"));
                        patientViewAppointmentDataList.add(patientViewAppointmentData);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }
                }
                adapter.notifyDataSetChanged();
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

    @Override
    public void onPatientAppointmentClick(int position) {
        PatientViewAppointmentData patientViewAppointmentData = patientViewAppointmentDataList.get(position);
        Intent intent = new Intent(this, PatientAppointmentDetails.class);
        intent.putExtra("extra_userid", String.valueOf(patientViewAppointmentData.getUser_id()));
        intent.putExtra("extra_appointmentid", String.valueOf(patientViewAppointmentData.getAppointment_id()));
        intent.putExtra("extra_doctorid", String.valueOf(patientViewAppointmentData.getDoctor_id()));
        intent.putExtra("extra_patientname", patientViewAppointmentData.getPatient_name());
        intent.putExtra("extra_appointmenttime", patientViewAppointmentData.getAppointment_time());
        intent.putExtra("extra_appointmentdate", patientViewAppointmentData.getAppointment_date());
        startActivity(intent);
    }
}
