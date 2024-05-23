package com.ceid.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ceid.model.service.TaxiRequest;
import com.ceid.util.Coordinates;
import com.ceid.util.Map;
import com.ceid.util.MapWrapperReadyListener;
import com.ceid.util.Timer;
import com.google.android.gms.maps.SupportMapFragment;

import java.time.Instant;

public class TransportScreen extends AppCompatActivity implements MapWrapperReadyListener {

    private Map map;
    Button button;
    Timer timer = new Timer();
    Instant startTimer;
    Instant stopTimer;
    TaxiRequest taxiRequest;

   protected  void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.transport_screen);

       Intent taxiRequestData = getIntent();
       taxiRequest = (TaxiRequest) taxiRequestData.getSerializableExtra("taxiRequest");

       button = findViewById(R.id.startEndRouteButton);
       SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.locationMapView);
       map = new Map(mapFragment, this, this);

       button.setOnClickListener(v -> startRoute());
   }

    @Override
    public void onMapWrapperReady() {
        map.placePin(taxiRequest.getPickupLocation(),true);
        map.setZoom(14);
        map.setPosition(taxiRequest.getPickupLocation());
    }

    public void startRoute(){
        Coordinates destination = new Coordinates( 38.246639, 21.734576);
        map.setZoom(13);
        map.setPosition(destination);
        String text="End Route";
        startTimer=timer.startTimer();
        button.setText(text);
        View.OnClickListener endRouteListener = v -> endRoute();
        button.setOnClickListener(endRouteListener);
    }

    public void endRoute(){
        stopTimer=timer.stopTimer();
        long elapsedTime=timer.elapsedTime(startTimer,stopTimer);
        Intent intent = new Intent(TransportScreen.this,MainScreenTaxi.class);
        startActivity(intent);

    }



}
