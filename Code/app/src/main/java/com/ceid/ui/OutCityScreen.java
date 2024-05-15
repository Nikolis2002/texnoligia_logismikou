package com.ceid.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ceid.util.Coordinates;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class OutCityScreen extends AppCompatActivity implements GarageAdapter.OnGarageItemListener, ActivityResultCallback<ActivityResult> {

    private RecyclerView recyclerView;
    private GarageAdapter adapter;
    private ArrayList<String> garageList;
    private Bundle locationScreenData = null;
    private Intent locationIntent;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.out_city_screen);

        this.activityResultLauncher = registerForActivityResult
        (
                new ActivityResultContracts.StartActivityForResult(),
                this
        );

        recyclerView=findViewById(R.id.recyclerViewGarage);
        garageList=new ArrayList<String>();
        adapter=new GarageAdapter(garageList,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void addGarage(String garage){
        garageList.add(garage);
        adapter.notifyItemInserted(garageList.size()-1);
    }

    @Override
    public void onItemClick(int position){
        String test=garageList.get(position);
        Intent intent=new Intent(OutCityScreen.this,OutCityVehiclesScreen.class);
        intent.putExtra("test",test);
        startActivity(intent);
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
            }
        }
    }

    public void onClick(View view)
    {
        if (locationScreenData != null)
            locationIntent.putExtras(locationScreenData);

        activityResultLauncher.launch(locationIntent);
    }
}
