package com.ceid.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class InCityScreen extends AppCompatActivity
{
    private Intent intent;
    private Intent taxiIntent;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.in_city_screen);

        //overridePendingTransition(0, 0);

        //Initialize tags
        findViewById(R.id.img_rent_car).setTag("car");
        findViewById(R.id.img_rent_motorcycle).setTag("motorcycle");
        findViewById(R.id.img_rent_bike).setTag("bike");
        findViewById(R.id.img_rent_scooter).setTag("scooter");

        //Initialize intent
        intent = new Intent(this, InCityVehicleScreen.class);
        taxiIntent = new Intent(this, TaxiSelect.class);
    }

    public void onClick(View view)
    {
        intent.putExtra("type", (String)view.getTag());
        startActivity(intent);
    }

    public void onTaxiClick(View view)
    {
        startActivity(taxiIntent);
    }
}


