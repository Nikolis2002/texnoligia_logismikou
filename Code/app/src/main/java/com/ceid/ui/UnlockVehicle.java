package com.ceid.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ceid.util.Coordinates;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.Timer;
import java.util.TimerTask;

public class UnlockVehicle extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private Coordinates car_loc;
    private int car_id;
    private Timer reservationTimer;


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unlock_screen);
        mapSetup();

        Intent data = getIntent();
        car_id = data.getIntExtra("vehicle_id",-1);
        car_loc = (Coordinates) data.getSerializableExtra("vehicle_location");

        reservationTimer = new Timer();

        reservationTimer.schedule(new TimerTask() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        },5000);

    }

    public void unlockVehicle(View view){
        IntentIntegrator qrScanner = new IntentIntegrator(UnlockVehicle.this);
        qrScanner.setCaptureActivity(QrCamera.class);
        qrScanner.setOrientationLocked(false);
        qrScanner.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        qrScanner.setPrompt("Scan QR on vehicle");
        qrScanner.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        IntentResult qrResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);

        if(qrResult!=null){
            String qr = qrResult.getContents();
            if(qr!=null){

            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void mapSetup(){
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add(R.id.googleMap,mapFragment).commit();
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map=googleMap;

        LatLng location = new LatLng(car_loc.getLat(),car_loc.getLng());
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(location,15));

        Marker carMarker = map.addMarker(new MarkerOptions()
                .position(location)
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (reservationTimer != null) {
            reservationTimer.cancel();
        }
    }
}
