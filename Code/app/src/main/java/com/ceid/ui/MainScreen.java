package com.ceid.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainScreen extends AppCompatActivity
{
    private Intent intent;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_screen);

        //Initialize tags
        findViewById(R.id.img_rent_car).setTag("car");
        findViewById(R.id.img_rent_motorcycle).setTag("motorcycle");
        findViewById(R.id.img_rent_bike).setTag("bike");
        findViewById(R.id.img_rent_scooter).setTag("scooter");

        //Initialize intent
        intent = new Intent(this, InCityVehicleScreen.class);
    }

    public void onClick(View view)
    {
        //Toast.makeText(this, String.format("I remember you're, %s", view.getTag()), Toast.LENGTH_SHORT).show();

        intent.putExtra("type", (String)view.getTag());
        startActivity(intent);
    }
}


