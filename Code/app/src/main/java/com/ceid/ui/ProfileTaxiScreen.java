package com.ceid.ui;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ceid.model.users.TaxiDriver;

public class ProfileTaxiScreen extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taxi_profile_screen);

        TaxiDriver taxiDriver=(TaxiDriver)((App) getApplicationContext()).getUser();

        TextView username = findViewById(R.id.usernameProfile);
        TextView name = findViewById(R.id.nameProfile);
        TextView surname = findViewById(R.id.surnameProfile);
        TextView email = findViewById(R.id.emailProfile);
        TextView amount = findViewById(R.id.amountProfile);

        username.setText(taxiDriver.getUsername());
        name.setText(taxiDriver.getName());
        surname.setText(taxiDriver.getLastname());
        email.setText(taxiDriver.getEmail());
        amount.setText(String.valueOf(taxiDriver.getWallet().getBalance()));
    }
}
