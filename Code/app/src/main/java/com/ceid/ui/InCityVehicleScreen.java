package com.ceid.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class InCityVehicleScreen extends AppCompatActivity implements OnMapReadyCallback
{
    private GoogleMap gmap;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.in_city_vehicle_screen);

        Bundle extras = getIntent().getExtras();
        assert extras != null;

        TextView textview = findViewById(R.id.in_city_choose_vehicle);

		textview.setText(String.format("%s %s", textview.getText(), extras.getString("type")));

        //Initialize map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);
		assert mapFragment != null;
		mapFragment.getMapAsync((OnMapReadyCallback) this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        gmap = googleMap;
    }
}
