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

public class ViewPatient extends AppCompatActivity implements ViewPatientAdapter.OnPatientListener {

    private TextView textView_userid;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private List<ViewPatientData> viewPatientDataList;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_patient);

        recyclerView = findViewById(R.id.RecylerView_viewpatient);
        viewPatientDataList = new ArrayList<>();
        adapter = new ViewPatientAdapter(getApplicationContext(), viewPatientDataList, this);
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

        getPatientList();
    }

    private void getPatientList() {

        final String user_id = textView_userid.getText().toString();
        final String patientUrl = "http://192.168.0.121/MobileRehab/viewpatient.php?user_id=" + user_id;
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Retrieving Data, Please Wait");
        progressDialog.show();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(patientUrl, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        ViewPatientData viewPatientData = new ViewPatientData();
                        viewPatientData.setUser_id(jsonObject.getInt("user_id"));
                        viewPatientData.setPatient_name(jsonObject.getString("patient_name"));
                        viewPatientData.setPatient_icnumber(jsonObject.getString("patient_icnumber"));
                        viewPatientData.setPatient_address(jsonObject.getString("patient_address"));
                        viewPatientData.setPatient_phonenumber(jsonObject.getString("patient_phonenumber"));

                        viewPatientDataList.add(viewPatientData);

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
    public void onPatientClick(int position) {

        ViewPatientData viewPatientData = viewPatientDataList.get(position);
        Intent intent = new Intent(this, ViewPatientDetails.class);
        intent.putExtra("extra_userid", String.valueOf(viewPatientData.getUser_id()));
        intent.putExtra("extra_patientname", viewPatientData.getPatient_name());
        intent.putExtra("extra_patienticnumber", viewPatientData.getPatient_icnumber());
        intent.putExtra("extra_patientaddress", viewPatientData.getPatient_address());
        intent.putExtra("extra_patientphonenumber", viewPatientData.getPatient_phonenumber());
        startActivity(intent);

    }
}
