package com.example.oops.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.oops.R;
import com.example.oops.Utils.AppCommon;
import com.example.oops.fragment.HomeFragment;

public class SplashScreen extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        handler();
    }

    private void handler() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(2000);
                    if (AppCommon.getInstance(SplashScreen.this).isUserLogIn()) {
                        startActivity(new Intent(SplashScreen.this, Dashboard.class));
                    } else {
                        startActivity(new Intent(SplashScreen.this, Login.class));
                    }
                    finish();
                } catch (Exception e) {

                }
            }
        };
        thread.start();
    }
}
