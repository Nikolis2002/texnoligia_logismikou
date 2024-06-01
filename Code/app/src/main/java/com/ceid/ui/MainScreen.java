package com.ceid.ui;
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

        //Disable back button
        //================================================================================
        OnBackPressedDispatcher dispatcher = getOnBackPressedDispatcher();
        dispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                //do nothing stay on the same screen
            }
        });

        //Bottom navigation
        //================================================================================

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new MainScreenFragment()).commit();
        this.customer=(Customer) User.getCurrentUser();

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
        //================================================================================
        if (customer.getWallet().isOverdrawn())
        {
            overdrawnError();
        }
        else
        {
            //Display InCityScreen
            Intent intent = new Intent(this, InCityScreen.class);
            startActivity(intent);
        }
    }

    public void toCoupons(View view)
    {
        //Display OfferScreen
        Intent intent = new Intent(this, OfferScreen.class);
        startActivity(intent);
    }

    public void outCity(View view)
    {
        //customer.getWallet().setBalance(-2);

        //Check if wallet is overdrawn
        //================================================================================
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
                return;
            }

            //Display OutCityScreen
            Intent intent = new Intent(this, OutCityScreen.class);
            startActivity(intent);
        }
    }
    public void addCardButton2(View view)
    {
        //Display PaymentMethodScreen
        Intent intent=new Intent(getApplicationContext(), PaymentMethodScreen.class);
        startActivity(intent);
    }

    public void chargeWallet(View view)
    {
        //Display ChargeWalletScreen
        Intent intent = new Intent(getApplicationContext(), ChargeWalletScreen.class);
        startActivity(intent);
    }

    public void addLicenseButton(View view)
    {
        //Display LicenseScreen
        Intent intent = new Intent(getApplicationContext(), LicenseScreen.class);
        startActivity(intent);
    }

    public void logout(View view)
    {
        User.wipeCurrentUser();

        Intent intent = new Intent(getApplicationContext(),Login.class);
        startActivity(intent);
        finish();
    }

    //ERRORS
    //========================================================================================

    public void overdrawnError()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Overdrawn Wallet");
        builder.setMessage("You have previous debt on your wallet and cannot proceed to our services. You can choose to charge your wallet now.");
        builder.setCancelable(false);

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
        builder.setCancelable(false);

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

   
