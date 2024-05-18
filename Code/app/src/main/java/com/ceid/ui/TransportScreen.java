package com.ceid.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ceid.util.Coordinates;
import com.ceid.util.Map;
import com.ceid.util.MapWrapperReadyListener;
import com.google.android.gms.maps.SupportMapFragment;

public class TransportScreen extends AppCompatActivity implements MapWrapperReadyListener {

    private Map map;

   protected  void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.transport_screen);

       SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.locationMapView);
       map = new Map(mapFragment, this, this);
   }



    @Override
    public void onMapWrapperReady() {
        Coordinates customer = new Coordinates( 38.246639, 21.734573);
        map.setZoom(14);
        map.setPosition(customer);
    }
}
