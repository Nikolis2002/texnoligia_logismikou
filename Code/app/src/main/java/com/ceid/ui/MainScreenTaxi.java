package com.ceid.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainScreenTaxi extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taxi_main_sceen);

    }

    public void taxiRequests(View view){
        Intent intent = new Intent(MainScreenTaxi.this,TaxiRequestsScreen.class);
        startActivity(intent);
    }

    public void profile(View view){
        Intent intent = new Intent(MainScreenTaxi.this, ProfileTaxiScreen.class);
        startActivity(intent);
    }

}
