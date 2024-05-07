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

import com.ceid.util.Coordinates;
import com.ceid.util.Map;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.textfield.TextInputEditText;

public class InCityVehicleScreen extends AppCompatActivity implements ActivityResultCallback<ActivityResult>
{

    private Intent locationIntent;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private Map map;
    private Coordinates selectedCoords = null;
    private Bundle locationScreenData = null;

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
        map = new Map(mapFragment, scrollView);
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

            //selectedCoords = (Coordinates) data.getSerializableExtra("coords");

            TextInputEditText text = findViewById(R.id.location_text);
            text.setText(String.format("%s %s", getResources().getString(R.string.location),locationScreenData.getSerializable("coords").toString()));
        }
    }
}
