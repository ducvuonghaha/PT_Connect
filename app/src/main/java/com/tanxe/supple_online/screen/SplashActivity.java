package com.tanxe.supple_online.screen;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.tanxe.supple_online.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Thread th = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(2500);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    Intent obj = new Intent(SplashActivity.this, SliderActivity.class);
                    startActivity(obj);
                }
            }
        };
        th.start();

        Bundle bundle=getIntent().getExtras();
        if (bundle!=null){

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}