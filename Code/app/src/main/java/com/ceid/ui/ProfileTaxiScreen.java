package com.ceid.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ceid.model.users.TaxiDriver;
import com.ceid.model.users.User;

public class ProfileTaxiScreen extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taxi_profile_screen);

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void Logout(View view){
        Intent intent = new Intent(ProfileTaxiScreen.this, Login.class);
        startActivity(intent);
        finish();
    }

}
