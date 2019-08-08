package com.example.mobilerehab;

import android.app.ProgressDialog;
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

public class ViewPatientDetails extends AppCompatActivity implements ViewPastAppointmentAdapter.OnPastAppointmentListener {

    TextView textView_userId, textView_patientname, textView_patienticnumber, textView_patientaddress, textView_patientphonenumber;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    DividerItemDecoration dividerItemDecoration;
    List<ViewPastAppointmentData> viewPastAppointmentDataList;
    RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_patient_details);

        textView_userId = findViewById(R.id.textView_userid);
        textView_patientname = findViewById(R.id.textView_patientname);
        textView_patienticnumber = findViewById(R.id.textView_patienticnumber);
        textView_patientaddress = findViewById(R.id.textView_patientaddress);
        textView_patientphonenumber = findViewById(R.id.textView_patientphonenumber);

        String patientid = getIntent().getExtras().getString("extra_userid");
        textView_userId.setText(patientid);
        String patientname = getIntent().getExtras().getString("extra_patientname");
        textView_patientname.setText(patientname);
        String patienticnumber = getIntent().getExtras().getString("extra_patienticnumber");
        textView_patienticnumber.setText(patienticnumber);
        String patientaddress = getIntent().getExtras().getString("extra_patientaddress");
        textView_patientaddress.setText(patientaddress);
        String patientphonenumber = getIntent().getExtras().getString("extra_patientphonenumber");
        textView_patientphonenumber.setText(patientphonenumber);


        recyclerView = findViewById(R.id.RecylerView_viewpastappointment);
        viewPastAppointmentDataList = new ArrayList<>();
        adapter = new ViewPastAppointmentAdapter(getApplicationContext(), viewPastAppointmentDataList, this);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);

        getPastAppointmentData();

    }

    private void getPastAppointmentData() {
        final String user_id = textView_userId.getText().toString();
        final String pastAppointmentUrl = "http://192.168.43.166/MobileRehab/appointment.php?selectFn=viewpastappointment&user_id=" + user_id;
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Retrieving Data, Please Wait");
        progressDialog.show();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(pastAppointmentUrl, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        ViewPastAppointmentData viewPastAppointmentData = new ViewPastAppointmentData();
                        viewPastAppointmentData.setAppointmentDate(jsonObject.getString("appointment_date"));
                        viewPastAppointmentData.setAppointmentTime(jsonObject.getString("appointment_time"));
                        viewPastAppointmentDataList.add(viewPastAppointmentData);

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
    public void onPastAppointmentClick(int position) {

    }
}
