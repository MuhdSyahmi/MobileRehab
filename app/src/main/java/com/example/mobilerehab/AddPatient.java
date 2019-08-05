package com.example.mobilerehab;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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

public class AddPatient extends AppCompatActivity {

    final String addpatientUrl = "http://192.168.1.23/MobileRehab/addpatient.php";
    Button button_signup;
    Vibrator v;
    EditText editText_patientemail, editText_patientpassword, editText_patientconfirmpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addpatient);

        editText_patientemail = findViewById(R.id.editText_patientemail);
        editText_patientpassword = findViewById(R.id.editText_patientpassword);
        editText_patientconfirmpassword = findViewById(R.id.editText_patientconfirmpassword);
        button_signup = findViewById(R.id.button_signup);
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        button_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateUserData();
            }
        });
    }

    private void validateUserData() {

        final String email_address = editText_patientemail.getText().toString();
        final String password = editText_patientpassword.getText().toString();

        if (TextUtils.isEmpty(email_address)) {
            editText_patientemail.setError("Please enter email");
            editText_patientemail.requestFocus();
            v.vibrate(100);
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editText_patientpassword.setError("Please enter password");
            editText_patientpassword.requestFocus();
            v.vibrate(100);
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email_address).matches()) {
            editText_patientemail.setError("Enter a valid email");
            editText_patientemail.requestFocus();
            v.vibrate(100);
            return;
        }

        String passwordvalidate = editText_patientpassword.getText().toString();
        String cpasswordvalidate = editText_patientconfirmpassword.getText().toString();

        if (!passwordvalidate.equals(cpasswordvalidate)) {
            editText_patientconfirmpassword.setError("Password Does not Match");
            editText_patientconfirmpassword.requestFocus();
            v.vibrate(100);
            return;
        }

        registerUser();

    }

    private void registerUser() {

        final String email_address = editText_patientemail.getText().toString();
        final String password = editText_patientpassword.getText().toString();
        final String user_id = SharedPref.getInstance(this).LoggedInUser();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, addpatientUrl,
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
                        Toast.makeText(getApplicationContext(),"Connection Error"+error, Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email_address",email_address);
                params.put("password", password);
                params.put("createdby", user_id);

                return params;
            }
        };
        VolleySingleton.getInstance(AddPatient.this).addToRequestQueue(stringRequest);
        startActivity(new Intent(getApplicationContext(), DoctorHome.class));
    }

}
