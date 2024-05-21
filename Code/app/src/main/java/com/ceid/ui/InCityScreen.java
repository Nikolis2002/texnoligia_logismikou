package com.ceid.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ceid.model.users.Customer;

public class InCityScreen extends AppCompatActivity
{
    private Intent intent;
    private Intent taxiIntent;
    Customer customer;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.in_city_screen);

        Intent userDataIntent = getIntent();
        customer= (Customer) userDataIntent.getSerializableExtra("customer");

        //overridePendingTransition(0, 0);

        //Initialize tags
        findViewById(R.id.img_rent_car).setTag("car");
        findViewById(R.id.img_rent_motorcycle).setTag("motorcycle");
        findViewById(R.id.img_rent_bike).setTag("bike");
        findViewById(R.id.img_rent_scooter).setTag("scooter");

        //Initialize intent
        intent = new Intent(this, InCityVehicleScreen.class);
        taxiIntent = new Intent(this, TaxiScreen.class);
    }

    public void onClick(View view)
    {
        intent.putExtra("type", (String)view.getTag());
        startActivity(intent);
    }

    public void onTaxiClick(View view)
    {
        taxiIntent.putExtra("customer",customer);
        startActivity(taxiIntent);
    }
}


