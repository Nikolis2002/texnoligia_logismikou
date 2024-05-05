package com.ceid.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class InCityVehicleScreen extends AppCompatActivity
{
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.in_city_vehicle_screen);

        Bundle extras = getIntent().getExtras();
        assert extras != null;

        TextView textview = findViewById(R.id.in_city_choose_vehicle);

		textview.setText(String.format("%s %s", textview.getText(), extras.getString("type")));
    }
}
