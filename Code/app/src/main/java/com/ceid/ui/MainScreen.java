package com.ceid.ui;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.ceid.model.users.Customer;
import com.ceid.model.users.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainScreen extends AppCompatActivity {
    Customer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new MainScreenFragment()).commit();
        customer = (Customer)((App) getApplicationContext()).getUser();
        User.setCurrentUser(customer);
        //Customer customer=(Customer) User.setCurrentUser(customer);
        //Bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                Fragment selectedFragment = null;

                if (id == R.id.page_home) {
                    selectedFragment = new MainScreenFragment();
                } else if (id == R.id.page_history) {
                    selectedFragment = new RouteHistory();
                } else if (id == R.id.page_profile) {
                    selectedFragment = new CustomerProfile();
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, selectedFragment).commit();

                return true; //successfully handled
            }
        });
    }
    
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {

    }

    public void inCity(View view) {

        //Check if wallet is overdrawn
        if (customer.getWallet().isOverdrawn())
        {
            //ERROR
        }
        else
        {
            Intent intent = new Intent(this, InCityScreen.class);
            startActivity(intent);
        }
    }

    public void outCity(View view) {
        Intent intent = new Intent(this, OutCityScreen.class);
        startActivity(intent);
    }
    public void addCardButton2(View view)
    {
        Intent intent=new Intent(getApplicationContext(),addCard.class);
        startActivity(intent);
    }
    public void chargeWallet(View view)
    {
        Intent intent = new Intent(getApplicationContext(), ChargeWalletScreen.class);
        startActivity(intent);
    }
    public void addLicenseButton(View view)
    {
        Intent intent = new Intent(getApplicationContext(),addLicense.class);
        startActivity(intent);
    }
    public void logout(View view)
    {
        finish();
        System.exit(0);
    }

}

   
