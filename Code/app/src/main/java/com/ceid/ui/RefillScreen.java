package com.ceid.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ceid.util.Map;
import com.google.android.gms.maps.SupportMapFragment;

public class RefillScreen extends AppCompatActivity {

    private Map map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.refill);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.locationMapViewRefill);

        map = new Map(mapFragment, this);
        
    }


}
