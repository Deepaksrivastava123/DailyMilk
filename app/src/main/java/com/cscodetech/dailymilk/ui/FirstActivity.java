package com.cscodetech.dailymilk.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.cscodetech.dailymilk.R;
import com.cscodetech.dailymilk.utility.SessionManager;

import static com.cscodetech.dailymilk.utility.SessionManager.intro;


public class FirstActivity extends AppCompatActivity {

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        sessionManager = new SessionManager(FirstActivity.this);
        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (sessionManager.getBooleanData(intro)) {
                        Intent openMainActivity = new Intent(FirstActivity.this, HomeActivity.class);
                        startActivity(openMainActivity);
                        finish();

                    } else {
                        Intent openMainActivity = new Intent(FirstActivity.this, IntroActivity.class);
                        startActivity(openMainActivity);
                        finish();
                    }

                }
            }
        };
        timer.start();
    }
}