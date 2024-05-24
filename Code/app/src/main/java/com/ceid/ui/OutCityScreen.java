package com.ceid.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ceid.model.transport.Garage;
import com.ceid.model.transport.OutCityCar;
import com.ceid.model.transport.OutCityTransport;
import com.ceid.model.transport.Rental;
import com.ceid.model.transport.Van;
import com.ceid.util.Coordinates;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class OutCityScreen extends AppCompatActivity implements AdapterView.OnItemClickListener, ActivityResultCallback<ActivityResult> {

    private RecyclerView recyclerView;
    private GarageListAdapterOld adapter;
    private ArrayList<Garage> garageList;
    private Bundle locationScreenData = null;
    private Intent locationIntent;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.out_city_screen);

        //Initialize activity result launcher
        this.activityResultLauncher = registerForActivityResult
        (
                new ActivityResultContracts.StartActivityForResult(),
                this
        );

        //Initialize location intent
        locationIntent = new Intent(this, LocationScreen.class);
    }

    public void onClick(View view)
    {
        if (locationScreenData != null)
            locationIntent.putExtras(locationScreenData);

        activityResultLauncher.launch(locationIntent);
    }

    private ArrayList<Garage> retrieveGarages()
    {
        //Communicate with database
        //...
        //...
        //...
        //Communication complete

        garageList = new ArrayList<>();

        ArrayList<OutCityTransport> l = new ArrayList<>();
        l.add(new OutCityCar("1", 20, 1, 1,"dista", "1", "2024"));
        l.add(new OutCityCar("2",20, 1, 1,"dista", "2", "2024"));
        l.add(new Van("3",20, 1, 1,"dista", "3", "2024"));
        l.add(new OutCityCar("4",20, 1,1, "dista", "4", "2024"));
        l.add(new Van("5",20, 1,1, "dista", "5", "2024"));
        l.add(new Van("6",20, 1,1, "dista", "6", "2024"));
        l.add(new Van("7",20, 1,1, "dista", "7", "2024"));
        l.add(new OutCityCar("8",20, 1,1, "dista", "8", "2024"));
        l.add(new OutCityCar("9",20, 1,1, "dista", "9", "2024"));
        l.add(new Van("10", 20,1,1, "dista", "10", "2024"));
        l.add(new Van("11", 20, 1,1, "dista", "11", "2024"));
        l.add(new OutCityCar("12",20, 1,1, "dista", "12", "2024"));
        l.add(new OutCityCar("13",20, 1, 1,"dista", "13", "2024"));
        l.add(new Van("14",20, 1,1, "dista", "14", "2024"));

        garageList.add(new Garage(
                0,
                "Garage #1",
                "Mitsou 17",
                new Coordinates(38.2442870,21.7326153),
                "Mon-Fri 08:00-20:00",
                l
        ));

        garageList.add(new Garage(
                1,
                "Garage #2",
                "Dista 1",
                new Coordinates(38.2466208,21.7325087),
                "Mon-Fri 08:00-20:00"
        ));

        garageList.add(new Garage(
                2,
                "Garage #3",
                "Odos 3",
                new Coordinates(38.2481327,21.7374738),
                "Mon-Fri 08:00-20:00"
        ));

        garageList.add(new Garage(
                2,
                "Garage #3",
                "Odos 3",
                new Coordinates(38.2481327,21.7374738),
                "Mon-Fri 08:00-20:00"
        ));

        garageList.add(new Garage(
                2,
                "Garage #3",
                "Odos 3",
                new Coordinates(38.2481327,21.7374738),
                "Mon-Fri 08:00-20:00"
        ));

        garageList.add(new Garage(
                2,
                "Garage #3",
                "Odos 3",
                new Coordinates(38.2481327,21.7374738),
                "Mon-Fri 08:00-20:00"
        ));

        garageList.add(new Garage(
                2,
                "Garage #3",
                "Odos 3",
                new Coordinates(38.2481327,21.7374738),
                "Mon-Fri 08:00-20:00"
        ));

        garageList.add(new Garage(
                2,
                "Garage #3",
                "Odos 3",
                new Coordinates(38.2481327,21.7374738),
                "Mon-Fri 08:00-20:00"
        ));

        garageList.add(new Garage(
                2,
                "Garage #3",
                "Odos 3",
                new Coordinates(38.2481327,21.7374738),
                "Mon-Fri 08:00-20:00"
        ));

        garageList.add(new Garage(
                2,
                "Garage #3",
                "Odos 3",
                new Coordinates(38.2481327,21.7374738),
                "Mon-Fri 08:00-20:00"
        ));

        garageList.add(new Garage(
                2,
                "Garage #3",
                "Odos 3",
                new Coordinates(38.2481327,21.7374738),
                "Mon-Fri 08:00-20:00"
        ));

        return garageList;
    }

    @Override
    public void onActivityResult(ActivityResult result)
    {
        if (result.getResultCode() == Activity.RESULT_OK)
        {
            Intent data = result.getData();
            locationScreenData = data.getExtras();

            Coordinates selectedCoords = (Coordinates) locationScreenData.getSerializable("coords");

            //Display selected location
            if (selectedCoords != null)
            {
                TextInputEditText text = findViewById(R.id.location_text);
                text.setText(String.format("%s %s", getResources().getString(R.string.location),selectedCoords.toString()));

                //Get garages
                this.garageList = retrieveGarages();

                if (garageList.isEmpty())
                {
                    //alternate flow
                }
                else
                {
                    //Add garages to list
                    ListView listView = (ListView) findViewById(R.id.listViewId);

                    listView.setAdapter(new GarageListAdapter(this,  garageList));
                    listView.setOnItemClickListener(this);
                }
            }
        }
    }

    /*
    private void addGarage(String garage){
        garageList.add(garage);
        adapter.notifyItemInserted(garageList.size()-1);
    }
    */

    //Clicking on list item
    public void onItemClick(AdapterView<?> parent, View clickedItem, int position, long id)
    {
        String test=garageList.get(position).getName();
        Intent intent=new Intent(OutCityScreen.this, GarageInfoScreen.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("garage", garageList.get(position));
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
