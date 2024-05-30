package com.ceid.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

import com.ceid.model.users.TaxiDriver;
import com.ceid.model.users.User;

public class ProfileTaxiScreen extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taxi_profile_screen);

        OnBackPressedDispatcher dispatcher = getOnBackPressedDispatcher();
        dispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });

        TaxiDriver taxiDriver=(TaxiDriver) User.getCurrentUser();

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


    public void Logout(View view){
        Intent intent = new Intent(ProfileTaxiScreen.this, Login.class);
        startActivity(intent);
        finish();
    }

}
