package com.ceid.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ceid.util.Coordinates;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class UnlockVehicle extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private Coordinates car_loc;
    private int car_id;


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unlock_screen);
        mapSetup();

        Intent data = getIntent();
        car_id = data.getIntExtra("car_id",-1);
        car_loc = (Coordinates) data.getSerializableExtra("car_location");

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
}
