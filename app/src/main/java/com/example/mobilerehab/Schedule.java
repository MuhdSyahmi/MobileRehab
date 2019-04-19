package com.example.mobilerehab;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Schedule extends AppCompatActivity implements ScheduleAdapter.OnScheduleListener {

    public static final String extra_scheduleid = "appointment_id";
    public static final String extra_patientname = "appointment_patientname";
    public static final String extra_scheduledate = "appointment_date";
    public static final String extra_scheduletime = "appointment_time";

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private List<ScheduleData> scheduleDataList;
    private RecyclerView.Adapter adapter;

    final String ScheduleUrl = "http://192.168.1.13/MobileRehab/appointment.php?selectFn=viewappointment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        recyclerView = findViewById(R.id.RecylerView_viewschedule);
        scheduleDataList = new ArrayList<>();
        adapter = new ScheduleAdapter(getApplicationContext(),scheduleDataList, this);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),linearLayoutManager.getOrientation());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);

        retrieveDataJSON();
    }

    private void retrieveDataJSON(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Retrieving Data, Please Wait");
        progressDialog.show();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(ScheduleUrl, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        ScheduleData scheduleData = new ScheduleData();
                        scheduleData.setSchedule_id(jsonObject.getInt("appointment_id"));
                        scheduleData.setSchedule_patientname(jsonObject.getString("appointment_patientname"));
                        scheduleData.setSchedule_date(jsonObject.getString("appointment_date"));
                        scheduleData.setSchedule_time(jsonObject.getString("appointment_time"));
                        scheduleDataList.add(scheduleData);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }
                }
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }
            }, new Response.ErrorListener(){
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
    public void onScheduleClick(int position) {
        ScheduleData scheduleData = scheduleDataList.get(position);
        Intent intent = new Intent(this, ScheduleInfo.class);

        intent.putExtra(extra_scheduleid, String.valueOf(scheduleData.getSchedule_id()));
        intent.putExtra(extra_patientname, scheduleData.getSchedule_patientname());
        intent.putExtra(extra_scheduledate, scheduleData.getSchedule_date());
        intent.putExtra(extra_scheduletime, scheduleData.getSchedule_time());
        startActivity(intent);
    }
}