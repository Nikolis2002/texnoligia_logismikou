package com.ceid.ui;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class endRide extends AppCompatActivity {
    private Bundle service;
    private TextView duration,cost,points;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.end_ride_screen);
        service=getIntent().getExtras();
        //service.
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();

    }
}

