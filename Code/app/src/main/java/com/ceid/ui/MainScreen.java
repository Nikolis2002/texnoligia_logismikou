package com.ceid.ui;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.ceid.model.users.Customer;
import com.ceid.model.users.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Objects;

public class MainScreen extends AppCompatActivity {
    Customer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        OnBackPressedDispatcher dispatcher = getOnBackPressedDispatcher();
        dispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                //do nothing stay on the same screen
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new MainScreenFragment()).commit();
        //User.setCurrentUser(customer);
        this.customer=(Customer) User.getCurrentUser();

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

    public void inCity(View view)
    {

        //customer.getWallet().setBalance(-2);

        //Check if wallet is overdrawn
        if (customer.getWallet().isOverdrawn())
        {
            overdrawnError();
        }
        else
        {
            Intent intent = new Intent(this, InCityScreen.class);
            startActivity(intent);
        }
    }

    public void outCity(View view)
    {
        //customer.getWallet().setBalance(-2);

        //Check if wallet is overdrawn
        if (customer.getWallet().isOverdrawn())
        {
            overdrawnError();
        }
        else
        {
            String license = customer.getLicense();

            //Check if the license is valid for out-city rental
            if (!Objects.equals(license, "CAR") && !Objects.equals(license, "BOTH"))
            {
                licenseErrorMsg();
            }
            else
            {
                Intent intent = new Intent(this, OutCityScreen.class);
                startActivity(intent);
            }
        }
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
        Intent intent = new Intent(getApplicationContext(), LicenseScreen.class);
        startActivity(intent);
    }
    public void logout(View view)
    {
        User.wipeCurrentUser();

        Intent intent = new Intent(getApplicationContext(),Login.class);
        startActivity(intent);
        finish();
        //System.exit(0);
    }

    //ERRORS
    //========================================================================================

    public void overdrawnError()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Overdrawn Wallet");
        builder.setMessage("You have previous debt on your wallet and cannot proceed to our services. You can choose to charge your wallet now.");

        builder.setPositiveButton("Charge Now", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                startActivity(new Intent(MainScreen.this, ChargeWalletScreen.class));
            }
        });

        builder.setNegativeButton("Maybe Later", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }

    public void licenseErrorMsg()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Invalid License");
        builder.setMessage("You don't have the necessary license (car) for this service. Would you like to insert a license now?");

        builder.setPositiveButton("Charge Now", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                startActivity(new Intent(MainScreen.this, LicenseScreen.class));
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }

}

   
