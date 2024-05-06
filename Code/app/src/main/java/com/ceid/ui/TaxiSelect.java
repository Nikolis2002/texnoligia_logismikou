package com.ceid.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class TaxiSelect extends AppCompatActivity{

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.taxi_select);

        Bundle extras = getIntent().getExtras();
        assert extras != null;


        
    }

}
