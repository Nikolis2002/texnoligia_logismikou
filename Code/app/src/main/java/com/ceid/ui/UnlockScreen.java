package com.ceid.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ceid.model.transport.Rental;
import com.ceid.util.Coordinates;
import com.ceid.util.Map;
import com.ceid.util.MapWrapperReadyListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.Timer;
import java.util.TimerTask;

public class UnlockScreen extends AppCompatActivity implements MapWrapperReadyListener {

    private Map map;
    private Rental rental;
    private int serviceId;
    private Timer reservationTimer;
    private static final int CAMERA_REQUEST_CODE = 200;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unlock_screen);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap);
        map = new Map(mapFragment, this, this);

        Intent data = getIntent();
        this.rental = (Rental)data.getSerializableExtra("vehicle");
        this.serviceId = data.getIntExtra("service_id", -1);


        reservationTimer = new Timer();

        reservationTimer.schedule(new TimerTask() {
            @Override
            public void run() {

                runOnUiThread(() -> {
                        Toast.makeText(getApplicationContext(), "Reservation time passed", Toast.LENGTH_SHORT).show();

                });
            }
        },5000);

    }

    public void openCamera(){
        IntentIntegrator qrScanner = new IntentIntegrator(UnlockScreen.this);
        qrScanner.setCaptureActivity(QrCamera.class);
        qrScanner.setOrientationLocked(false);
        qrScanner.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        qrScanner.setPrompt("Scan QR on vehicle");
        qrScanner.initiateScan();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {

    }

    public void cancelReservation(){
        
    }

    public void unlockVehicle(View view){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
                openCamera();
        }else {
            ActivityCompat.requestPermissions(UnlockScreen.this, new String[]{Manifest.permission.CAMERA},
                    CAMERA_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
               openCamera();
            }else{
                Toast.makeText(getApplicationContext(), "App do not have camera permission", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this,MainScreen.class);
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        IntentResult qrResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (qrResult != null) {
            String qr = qrResult.getContents();
            if (qr != null) {
                Toast.makeText(getApplicationContext(), qr, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void cancelReservation(View view){
        Intent intent = new Intent(this,MainScreen.class);
        startActivity(intent);
    }

    @Override
    public void onMapWrapperReady()
    {
        Coordinates location = new Coordinates(rental.getTracker().getCoords().getLat(),rental.getTracker().getCoords().getLng());
        map.setZoom(15);
        map.setPosition(location);
        map.placePin(location,true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (reservationTimer != null) {
            reservationTimer.cancel();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (reservationTimer != null) {
            reservationTimer.cancel();
        }
    }
}
