package com.ceid.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.ceid.model.transport.CityCar;
import com.ceid.model.transport.Garage;
import com.ceid.model.transport.OutCityCar;
import com.ceid.model.transport.OutCityTransport;
import com.ceid.model.transport.Van;

import java.util.ArrayList;

public class GarageInfoScreen extends AppCompatActivity implements AdapterView.OnItemClickListener
{

    private ArrayList<OutCityTransport> vehicles;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.garage_info_screen);

        Garage garage = (Garage) getIntent().getExtras().getSerializable("garage");
        assert garage != null;

        TextView name = findViewById(R.id.nameField);
        TextView address = findViewById(R.id.addressField);
        TextView hours = findViewById(R.id.hoursField);

        name.setText(garage.getName());
        address.setText(garage.getAddress());
        hours.setText(garage.getAvailableHours());

        //Add vehicles to list
        ListView listView = (ListView) findViewById(R.id.listViewId);

        //GET VEHICLES
        this.vehicles = garage.getVehicles();

        listView.setAdapter(new OutCityVehicleListAdapter(this, this.vehicles));
        listView.setOnItemClickListener(this);
    }

    public void back(View view)
    {
        finish();
    }

    //Choose vehicle
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
    {
        if (vehicles.isEmpty())
            return;

        OutCityTransport vehicle = vehicles.get(i);

        Intent intent = new Intent(this, GarageReservationForm.class);
        intent.putExtra("vehicle", vehicle);
        startActivity(intent);
    }
}
