package com.ceid.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainScreen extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
    }

    public void inCity(View view)
    {
        Intent intent = new Intent(this, InCityScreen.class);
        startActivity(intent);
    }

    public void outCity(View view)
    {
        Intent intent = new Intent(this, OutCityScreen.class);
        startActivity(intent);
    }
}
