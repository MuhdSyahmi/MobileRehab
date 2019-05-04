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

    EditText editText_loginusername, editText_loginpassword;
    final String loginURL = "http://10.131.73.39/MobileRehab/login.php";
    Button button_login;
    Vibrator v;
    TextView textView_roles, textView_userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editText_loginusername = findViewById(R.id.editText_loginusername);
        editText_loginpassword = findViewById(R.id.editText_loginpassword);
        textView_roles = findViewById(R.id.textView_roles);
        textView_userid = findViewById(R.id.textView_userid);
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

        final String email_address = editText_loginusername.getText().toString();
        final String password = editText_loginpassword.getText().toString();

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

        final String email_address = editText_loginusername.getText().toString();
        final String password = editText_loginpassword.getText().toString();
        final String roles = textView_roles.getText().toString();
        final String user_id = textView_userid.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,loginURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();

                            JSONObject obj = new JSONObject(response);
                            if (obj.getBoolean("error")) {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            } else {

                                String Username = obj.getString("email_address");
                                Toast.makeText(getApplicationContext(),Username, Toast.LENGTH_SHORT).show();
                                SharedPref.getInstance(getApplicationContext()).storeUserName(Username);

                                String UserID = obj.getString("user_id");
                                SharedPref.getInstance(getApplicationContext()).storeUserId(UserID);

                                String roles = obj.getString("roles");

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
