package com.ceid.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.ceid.model.transport.Garage;

public class GarageInfoScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.garage_info_screen);

        Garage garage = (Garage) getIntent().getExtras().getSerializable("garage");

        Log.d("GARAGE", garage.getName());
    }

}
