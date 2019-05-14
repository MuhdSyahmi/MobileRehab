package com.example.mobilerehab;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import static com.example.mobilerehab.SharedPref.SHARED_PREF_NAME;

public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        final Thread thread = new Thread() {

            @Override
            public void run() {

                try {

                    sleep(2000);
                    SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
                    String status = sharedPreferences.getString("user_id", "");
                    if (status.isEmpty()) {

                        Intent intent = new Intent(SplashScreen.this, Login.class);
                        startActivity(intent);
                        finish();
                    } else {

                        String roles = sharedPreferences.getString("roles", "");

                        if (roles.equals("Doctor")) {

                            startActivity(new Intent(getApplicationContext(), DoctorHome.class));

                        } else if (roles.equals("Patient")) {

                            startActivity(new Intent(getApplicationContext(), PatientHome.class));

                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();

                }
            }

        };

        thread.start();
    }
}
