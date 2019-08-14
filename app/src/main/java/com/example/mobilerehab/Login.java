package com.example.mobilerehab;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

public class Login extends AppCompatActivity implements View.OnClickListener {

    final String loginURL = "http://192.168.0.121/MobileRehab/login.php";
    TextInputLayout editText_loginusername, editText_loginpassword;
    Button button_login;
    Vibrator v;
    TextView textView_roles, textView_userid, textView_createdby;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editText_loginusername = findViewById(R.id.textinput_email);
        editText_loginpassword = findViewById(R.id.textinput_password);
        textView_roles = findViewById(R.id.textView_roles);
        textView_userid = findViewById(R.id.textView_userid);
        textView_createdby = findViewById(R.id.textView_createdby);
        button_login = findViewById(R.id.button_login);
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateUserData();
            }
        });

    }
    private void validateUserData() {

        final String email_address = editText_loginusername.getEditText().getText().toString();
        final String password = editText_loginpassword.getEditText().getText().toString();

        if (TextUtils.isEmpty(email_address)) {
            editText_loginusername.setError("Please enter your email");
            editText_loginpassword.requestFocus();
            v.vibrate(100);
            button_login.setEnabled(true);
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editText_loginpassword.setError("Please enter your password");
            editText_loginpassword.requestFocus();
            v.vibrate(100);
            button_login.setEnabled(true);
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email_address).matches()) {
            editText_loginusername.setError("Enter a valid email");
            editText_loginusername.requestFocus();
            v.vibrate(100);
            button_login.setEnabled(true);
            return;
        }
        loginUser();
    }

    private void loginUser() {

        final String email_address = editText_loginusername.getEditText().getText().toString();
        final String password = editText_loginpassword.getEditText().getText().toString();
        final String roles = textView_roles.getText().toString();
        final String user_id = textView_userid.getText().toString();
        final String created_by = textView_createdby.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,loginURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();

                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("error")) {
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            } else {

                                String Username = jsonObject.getString("email_address");
                                Toast.makeText(getApplicationContext(),Username, Toast.LENGTH_SHORT).show();
                                SharedPref.getInstance(getApplicationContext()).storeUserName(Username);

                                String CreatedBy = jsonObject.getString("createdby");
                                SharedPref.getInstance(getApplicationContext()).storeCreatedBy(CreatedBy);

                                String UserID = jsonObject.getString("user_id");
                                SharedPref.getInstance(getApplicationContext()).storeUserId(UserID);


                                String roles = jsonObject.getString("roles");
                                SharedPref.getInstance(getApplicationContext()).storeRoles(roles);

                                if(roles.equals("Doctor"))
                                {
                                    startActivity(new Intent(getApplicationContext(), DoctorHome.class));
                                }else if(roles.equals("Patient"))
                                {
                                    startActivity(new Intent(getApplicationContext(), PatientHome.class));
                                }

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

                params.put("user_id", user_id);
                params.put("email_address", email_address);
                params.put("password", password);
                params.put("roles", roles);
                params.put("createdby", created_by);

                return params;

            }
        };
        VolleySingleton.getInstance(Login.this).addToRequestQueue(stringRequest);
    }

    @Override
    public void onClick(View v) {
        loginUser();
    }
}
