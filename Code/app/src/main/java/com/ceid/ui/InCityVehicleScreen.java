package com.ceid.ui;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ceid.model.payment_methods.Currency;
import com.ceid.model.transport.CityCar;
import com.ceid.model.transport.Rental;
import com.ceid.util.Coordinates;
import com.ceid.util.DateFormat;
import com.ceid.util.Map;
import com.ceid.util.MapWrapperReadyListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashMap;

public class InCityVehicleScreen extends AppCompatActivity implements ActivityResultCallback<ActivityResult>, MapWrapperReadyListener
{

    private Intent locationIntent;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private Map map;
    private Coordinates selectedCoords = null;
    private Bundle locationScreenData = null;

    private int markerIcon;

    private ArrayList<Rental> vehicleList;

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

        //Initialize location intent
        this.activityResultLauncher = registerForActivityResult
        (
            new ActivityResultContracts.StartActivityForResult(),
            this
        );

        //Initialize intents
        locationIntent = new Intent(this, LocationScreen.class);

        //Initialize map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
        NestedScrollView scrollView = findViewById(R.id.mainScrollView);
        map = new Map(mapFragment, scrollView, this);

        this.vehicleList = new ArrayList<>();

        switch(extras.getString("type"))
        {
            case "car": this.markerIcon = R.drawable.in_city_car; break;
            case "motorcycle": this.markerIcon = R.drawable.in_city_motorcycle; break;
            case "bike": this.markerIcon = R.drawable.in_city_bicycle; break;
            case "scooter": this.markerIcon = R.drawable.in_city_scooter; break;
        }

        //====================================================================================
    }

    //TESTING
    @Override
    public void onMapWrapperReady()
    {

    }

    //Clicking on the location screen
    public void onClick(View view)
    {
        if (locationScreenData != null)
            locationIntent.putExtras(locationScreenData);

        activityResultLauncher.launch(locationIntent);
    }

    //After you return from the location screen
    @Override
    public void onActivityResult(ActivityResult result)
    {
        if (result.getResultCode() == Activity.RESULT_OK)
        {
            Intent data = result.getData();
            locationScreenData = data.getExtras();

            selectedCoords = (Coordinates) locationScreenData.getSerializable("coords");

            //Display selected location
            if (selectedCoords != null)
            {
                TextInputEditText text = findViewById(R.id.location_text);
                text.setText(String.format("%s %s", getResources().getString(R.string.location),selectedCoords.toString()));
            }

            //Set view around selected position
            map.setZoom(locationScreenData.getFloat("zoom"));
            map.setPosition(selectedCoords);

            //Retrieve vehicles
            this.vehicleList = this.getVehicles();

            map.placePin(selectedCoords, true);

            //Place pins
            for (Rental rental : vehicleList)
            {
                if (selectedCoords.withinRadius(rental.getTracker().getCoords(), 2000) && rental.isFree())
                    map.placePin(rental.getTracker().getCoords(), false, markerIcon);
            }
        }
    }

    public ArrayList<Rental> getVehicles()
    {
        ArrayList<Rental> vehicleList = new ArrayList<>();

        //Communicate with database
        //...
        //...
        //...
        //Communication complete

        vehicleList.add(new CityCar(
                "ABC-1234",
                true,
                0,
                "MONDEO",
                "FORD",
                "1993",
                null,
                new Currency(2),
                new Coordinates(38.2442870,21.7326153))
        );

        vehicleList.add(new CityCar(
                "DEF-1234",
                true,
                1,
                "CIVIC",
                "HONDA",
                "2006",
                null,
                new Currency(3),
                new Coordinates(38.2466208,21.7325087))
        );

        vehicleList.add(new CityCar(
                "GHI-1234",
                true,
                2,
                "AZTEK",
                "PONTIAC",
                "2004",
                null,
                new Currency(4),
                new Coordinates(38.2481327,21.7374738))
        );

        vehicleList.add(new CityCar(
                "JKL-1234",
                false,
                3,
                "ESTEEM",
                "SUZUKI",
                "1998",
                null,
                new Currency(4.28),
                new Coordinates(38.2442388,21.7405935))
        );

        vehicleList.add(new CityCar(
                "2-GO",
                true,
                4,
                "PATTY WAGON",
                "KRUSTY KRAB",
                "2004",
                null,
                new Currency(5),
                new Coordinates(38.2473288,21.6084180))
        );

        return vehicleList;
    }
}
