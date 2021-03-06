package com.example.ldrp.medeeasyapp;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.facebook.stetho.Stetho;

public class SplashActivity extends AppCompatActivity {


    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Stetho.initializeWithDefaults(this);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                final Intent gotoLoginScreen = new Intent(SplashActivity.this, LoginSelectionActivity.class);
                startActivity(gotoLoginScreen);
                finish();
            }
        };
        handler.postDelayed(runnable, 3000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }

}