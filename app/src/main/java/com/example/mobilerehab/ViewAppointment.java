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

public class ViewAppointment extends AppCompatActivity implements ViewAppointmentAdapter.OnAppointmentListener {

    private TextView textView_userid;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private List<ViewAppointmentData> viewAppointmentDataList;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_appointment);

        recyclerView = findViewById(R.id.RecylerView_viewappointment);
        viewAppointmentDataList = new ArrayList<>();
        adapter = new ViewAppointmentAdapter(getApplicationContext(), viewAppointmentDataList, this);
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

        getAppointmentList();
    }

    private void getAppointmentList() {

        final String user_id = textView_userid.getText().toString();
        final String appointmentUrl = "http://192.168.1.48/MobileRehab/appointment.php?selectFn=viewappointment&appointment_doctorid=" + user_id;
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Retrieving Data, Please Wait");
        progressDialog.show();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(appointmentUrl, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        ViewAppointmentData viewAppointmentData = new ViewAppointmentData();
                        viewAppointmentData.setAppointment_id(jsonObject.getString("appointment_id"));
                        viewAppointmentData.setPatient_name(jsonObject.getString("patient_name"));
                        viewAppointmentData.setAppointment_time(jsonObject.getString("appointment_time"));
                        viewAppointmentData.setAppointment_date(jsonObject.getString("appointment_date"));
                        viewAppointmentData.setAppointment_status(jsonObject.getString("appointment_status"));
                        viewAppointmentDataList.add(viewAppointmentData);

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
    public void onAppointmentClick(int position) {
        ViewAppointmentData viewAppointmentData = viewAppointmentDataList.get(position);
        Intent intent = new Intent(this, AppointmentDetails.class);
        intent.putExtra("extra_appointmentid", viewAppointmentData.getAppointment_id());
        intent.putExtra("extra_patientname", viewAppointmentData.getPatient_name());
        intent.putExtra("extra_appointmenttime", viewAppointmentData.getAppointment_time());
        intent.putExtra("extra_appointmentdate", viewAppointmentData.getAppointment_date());
        intent.putExtra("extra_appointmentstatus", viewAppointmentData.getAppointment_status());
        startActivity(intent);
    }
}
