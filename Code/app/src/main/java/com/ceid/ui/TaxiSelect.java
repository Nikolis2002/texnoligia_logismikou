package com.ceid.ui;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TaxiSelect extends AppCompatActivity{

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.taxi_select);

        TextView estCost=findViewById(R.id.estimatedCost);
        estCost.setText("test");
        
    }

}
