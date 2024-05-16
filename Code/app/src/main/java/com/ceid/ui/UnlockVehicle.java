package com.ceid.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
    private static final int CAMERA_REQUEST_CODE = 200;

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

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {

    }


    public void unlockVehicle(View view){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            IntentIntegrator qrScanner = new IntentIntegrator(UnlockVehicle.this);
            qrScanner.setCaptureActivity(QrCamera.class);
            qrScanner.setOrientationLocked(false);
            qrScanner.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
            qrScanner.setPrompt("Scan QR on vehicle");
            qrScanner.initiateScan();
        }else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    CAMERA_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                IntentIntegrator qrScanner = new IntentIntegrator(UnlockVehicle.this);
                qrScanner.setCaptureActivity(QrCamera.class);
                qrScanner.setOrientationLocked(false);
                qrScanner.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                qrScanner.setPrompt("Scan QR on vehicle");
                qrScanner.initiateScan();
            } else {
                Toast.makeText(this, "Camera permission required", Toast.LENGTH_SHORT).show();
            }
        }
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
