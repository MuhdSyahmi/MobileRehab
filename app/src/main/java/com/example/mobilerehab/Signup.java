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

public class Signup extends AppCompatActivity {

    EditText editText_signupemail, editText_signuppassword;
    Button button_signup;
    Vibrator v;

    final String registerUrl = "http://192.168.1.33/MobileRehab/register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editText_signupemail = findViewById(R.id.editText_signupemail);
        editText_signuppassword = findViewById(R.id.editText_signuppassword);
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

        final String email_address = editText_signupemail.getText().toString();
        final String password = editText_signuppassword.getText().toString();

        if (TextUtils.isEmpty(email_address)) {
            editText_signupemail.setError("Please enter email");
            editText_signupemail.requestFocus();
            // Vibrate for 100 milliseconds
            v.vibrate(100);
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editText_signuppassword.setError("Please enter password");
            editText_signuppassword.requestFocus();
            //Vibrate for 100 milliseconds
            v.vibrate(100);
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email_address).matches()) {
            editText_signupemail.setError("Enter a valid email");
            editText_signupemail.requestFocus();
            //Vibrate for 100 milliseconds
            v.vibrate(100);
            return;
        }

//        if (!reg_password.equals(reg_cpassword)) {
//            password.setError("Password Does not Match");
//            password.requestFocus();
//            //Vibrate for 100 milliseconds
//            v.vibrate(100);
//            return;
//        }

        registerUser();

    }

    private void registerUser() {

        final String email_address = editText_signupemail.getText().toString();
        final String password = editText_signuppassword.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,registerUrl,
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
                params.put("selectFn","addpatient");
                params.put("email_address",email_address);
                params.put("password", password);

                return params;
            }
        };
        VolleySingleton.getInstance(Signup.this).addToRequestQueue(stringRequest);
    }

}
