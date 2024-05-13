package com.ceid.ui;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ceid.model.payment_methods.Currency;
import com.ceid.model.transport.CityCar;
import com.ceid.model.transport.Rental;
import com.ceid.util.Coordinates;
import com.ceid.util.Map;
import com.ceid.util.MapWrapperReadyListener;
import com.ceid.util.PositiveInteger;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class InCityVehicleScreen extends AppCompatActivity implements ActivityResultCallback<ActivityResult>, MapWrapperReadyListener, AdapterView.OnItemClickListener, GoogleMap.OnMarkerClickListener
{

    private Intent locationIntent;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private Map map;
    private Coordinates selectedCoords = null;
    private Bundle locationScreenData = null;

    private String type;
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
        map = new Map(mapFragment, this, this);
        map.setMarkerListener(this);

        this.vehicleList = new ArrayList<>();

        this.type = extras.getString("type");

        TextView tview = (TextView) findViewById(R.id.nearbyText);

        switch(type)
        {
            case "car":
            {
                this.markerIcon = R.drawable.in_city_car;
                tview.setText(R.string.cars_nearby);
            }
            break;

            case "motorcycle":
            {
                this.markerIcon = R.drawable.in_city_motorcycle;
                tview.setText(R.string.motorcycles_nearby);
            }
            break;

            case "bike":
            {
                this.markerIcon = R.drawable.in_city_bicycle;
                tview.setText(R.string.bicycles_nearby);
            }
            break;

            case "scooter":
            {
                this.markerIcon = R.drawable.in_city_scooter;
                tview.setText(R.string.scooters_nearby);
            }
            break;
        }
    }

    //TESTING
    @Override
    public void onMapWrapperReady()
    {
        Coordinates Patra = new Coordinates( 38.246639, 21.734573);
        map.setZoom(12);
        map.setPosition(Patra);
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker)
    {
        Object tag  = marker.getTag();

        if (tag != null) //Vehicle marker
        {
            // Inflate the popup window layout
            View popupView = LayoutInflater.from(this).inflate(R.layout.vehicle_popup, null);

            // Create the popup window
            PopupWindow popupWindow = new PopupWindow(
                    popupView,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );

            // Show the popup window
            popupWindow.showAtLocation(
                    findViewById(android.R.id.content),
                    Gravity.CENTER,
                    0,
                    0
            );

            TextView title = popupView.findViewById(R.id.text_title);
            TextView dista = popupView.findViewById(R.id.text_dista);
            TextView rate = popupView.findViewById(R.id.text_rate);
            TextView seats = popupView.findViewById(R.id.text_seats);
            TextView plate = popupView.findViewById(R.id.text_plate);

            CityCar car = (CityCar)tag;

            title.setText(String.format("%s %s (%s)", car.getManufacturer(), car.getModel(), car.getManufYear()));
            dista.setText(String.format("%s: %d m", "Distance", Math.round(selectedCoords.distance(car.getTracker().getCoords()))));
            rate.setText(String.format("%s: %s/min", "Rate", car.getRate().toString()));
            seats.setText(String.format("%s: %d", "Seats", 4));
            plate.setText(String.format("%s: %s", "License Plate", car.getLicencePlate()));


            Button cancel = popupView.findViewById(R.id.cancel);
            Button reserve = popupView.findViewById(R.id.reserve);

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    popupWindow.dismiss();
                }
            });

            reserve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(v.getContext(), UnlockVehicle.class);
                    intent.putExtra("car_id",car.getId());
                    intent.putExtra("car_location", car.getTracker().getCoords());
                    startActivity(intent);
                }
            });
        }

        return true;

    }

    //Clicking on list item
    public void onItemClick(AdapterView<?> parent, View clickedItem, int position, long id)
    {
        //Log.d("CLICK", String.format("Position: %d", position));

        Rental rental = (Rental) clickedItem.getTag();
        map.smoothTransition(rental.getTracker().getCoords());
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

                //Set view around selected position
                map.setZoom(locationScreenData.getFloat("zoom"));
                map.setPosition(selectedCoords);

                //Retrieve vehicles
                this.vehicleList = this.getVehicles();

                map.placePin(selectedCoords, true);

                ArrayList<Rental> validVehicles = new ArrayList<>();

                //Place pins
                for (Rental rental : vehicleList)
                {
                    if (selectedCoords.withinRadius(rental.getTracker().getCoords(), 2000) && rental.isFree())
                    {
                        Marker marker = map.placePin(rental.getTracker().getCoords(), false, markerIcon);
                        marker.setTag(rental);
                        validVehicles.add(rental);
                    }
                }

                ListView listView = (ListView) findViewById(R.id.listViewId);

                listView.setAdapter(new VehicleListAdapter(this,  validVehicles, this.markerIcon, selectedCoords));
                listView.setOnItemClickListener(this);
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
                new Currency(1.40),
                new Coordinates(38.2442870,21.7326153),
                new PositiveInteger(0)
        ));

        vehicleList.add(new CityCar(
                "DEF-1234",
                true,
                1,
                "CIVIC",
                "HONDA",
                "2006",
                null,
                new Currency(1.30),
                new Coordinates(38.2466208,21.7325087),
                new PositiveInteger(0)
        ));

        vehicleList.add(new CityCar(
                "GHI-1234",
                true,
                2,
                "AZTEK",
                "PONTIAC",
                "2004",
                null,
                new Currency(1.20),
                new Coordinates(38.2481327,21.7374738),
                new PositiveInteger(0)
        ));

        vehicleList.add(new CityCar(
                "JKL-1234",
                true,
                3,
                "ESTEEM",
                "SUZUKI",
                "1998",
                null,
                new Currency(1.00),
                new Coordinates(38.2442388,21.7405935),
                new PositiveInteger(0)
        ));

        vehicleList.add(new CityCar(
                "2-GO",
                true,
                4,
                "PATTY WAGON",
                "KRUSTY KRAB",
                "2004",
                null,
                new Currency(5.00),
                new Coordinates(38.2473288,21.6084180),
                new PositiveInteger(0)
        ));

        return vehicleList;
    }
}
