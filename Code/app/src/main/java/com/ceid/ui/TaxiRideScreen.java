package com.ceid.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class TaxiRideScreen extends AppCompatActivity {

    private Chronometer timer;
    private boolean status=false;
    private Handler handler;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taxi_ride_screen);
        handler = new Handler();
        rideStatus();
        timer = findViewById(R.id.timer);
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();
    }

    Runnable taxiRideCheck = new Runnable() {
        @Override
        public void run() {

            if(status) {
                Intent intent = new Intent(TaxiRideScreen.this, MainScreen.class);
                startActivity(intent);
            }else{
                Toast.makeText(getApplicationContext(), "No!!!", Toast.LENGTH_SHORT).show();
            }
            handler.postDelayed(this, 2000);
        }
    };

    public  void rideStatus() {
        handler.post(taxiRideCheck);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(taxiRideCheck);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(taxiRideCheck);
    }

}
