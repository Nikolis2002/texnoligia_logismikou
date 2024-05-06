package com.ceid.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ceid.util.Map;

public class InCityVehicleScreen extends AppCompatActivity
{
    private Intent locationIntent;
    private Map map;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.in_city_vehicle_screen);

        Bundle extras = getIntent().getExtras();
        assert extras != null;

        //Initialize header text
        TextView textview = findViewById(R.id.in_city_choose_vehicle);
		textview.setText(String.format("%s %s", textview.getText(), extras.getString("type")));

        //Initialize intents
        locationIntent = new Intent(this, LocationScreen.class);

        //Initialize map
        ScrollMapFragment mapFragment = (ScrollMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
        NestedScrollView scrollView = findViewById(R.id.mainScrollView);

        map = new Map(mapFragment, scrollView);
    }

    public void onClick(View view)
    {
        startActivity(locationIntent);
    }
}
