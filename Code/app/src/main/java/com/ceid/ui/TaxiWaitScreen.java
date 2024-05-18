package com.ceid.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TaxiWaitScreen extends AppCompatActivity {

    private Handler handler;
    private boolean status=false;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taxi_request_wait_screen);

        handler = new Handler();
        rideStatus();

        Timer reservationTimer = new Timer();

        reservationTimer.schedule(new TimerTask() {
            @Override
            public void run() {

                runOnUiThread(() -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(TaxiWaitScreen.this);
                    builder.setMessage("Select a option");
                    builder.setTitle("Taxi do not found");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Resend", (alertDialog, which) -> {

                        Intent intent = new Intent(TaxiWaitScreen.this, TaxiWaitScreen.class);
                        startActivity(intent);
                    });
                    builder.setNegativeButton("Cancel", (alertDialog, which) -> {

                        Intent intent = new Intent(TaxiWaitScreen.this, InCityScreen.class);
                        startActivity(intent);
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                });
            }
        },10000);
    }

    Runnable taxiFoundCheck = new Runnable() {
        @Override
        public void run() {

            if(!status) {
                Intent intent = new Intent(TaxiWaitScreen.this, TaxiRideScreen.class);
                startActivity(intent);
            }else{
                Toast.makeText(getApplicationContext(), "No!!", Toast.LENGTH_SHORT).show();
            }
            handler.postDelayed(this, 2000);
        }
    };

    public  void rideStatus() {
        handler.post(taxiFoundCheck);
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(taxiFoundCheck);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(taxiFoundCheck);
    }
}
