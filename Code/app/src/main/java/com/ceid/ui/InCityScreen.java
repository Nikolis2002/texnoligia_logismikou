package com.ceid.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaRouter;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.ceid.model.transport.Bicycle;
import com.ceid.model.transport.CityCar;
import com.ceid.model.transport.ElectricScooter;
import com.ceid.model.transport.Motorcycle;
import com.ceid.model.transport.Rental;
import com.ceid.model.users.Customer;
import com.ceid.model.users.User;

public class InCityScreen extends AppCompatActivity
{
    private Intent rentalIntent;
    private Intent taxiIntent;
    Customer customer;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.in_city_screen);

        customer = (Customer)User.getCurrentUser();

        //Initialize tags with dummy vehicles
        //Use these dummy objects later to see if the selected vehicle requires license
        //================================================================================
        findViewById(R.id.img_rent_car).setTag(new CityCar());
        findViewById(R.id.img_rent_motorcycle).setTag(new Motorcycle());
        findViewById(R.id.img_rent_bike).setTag(new Bicycle());
        findViewById(R.id.img_rent_scooter).setTag(new ElectricScooter());

        //Initialize intent
        //================================================================================
        rentalIntent = new Intent(this, InCityVehicleScreen.class);
        taxiIntent = new Intent(this, TaxiScreen.class);
    }

    public void onVehicleSelect(View view)
    {
        Rental selectedVehicle = (Rental)view.getTag();

        //Type of vehicle
        //Pass this info to the next screen
        //================================================================================
        String type;

        if (selectedVehicle instanceof CityCar) type="car";
        else if (selectedVehicle instanceof Motorcycle) type="motorcycle";
        else if (selectedVehicle instanceof Bicycle) type="bike";
        else type="scooter";

        rentalIntent.putExtra("type", type);

        //Check if vehicle requires license
        //================================================================================
        if (selectedVehicle.requiresLicense())
        {
            String license = customer.getLicense();

            if (license == null)
            {
                //Ask the user if he wants to insert license
                //================================================================================
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setTitle("Missing License");
                builder.setMessage("The selected vehicle requires a driver's license, but you don't have one");
                builder.setCancelable(false);

                //User inserts license
                //================================================================================
                builder.setPositiveButton("Insert", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        //Go to LicenseScreen
                        startActivity(new Intent(InCityScreen.this, LicenseScreen.class));
                    }
                });

                //User doesn't insert license
                //================================================================================
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                        finish();
                    }
                });

                AlertDialog alert = builder.create();
                alert.setCanceledOnTouchOutside(false);
                alert.show();

                return;
            }
            else
            {
                //Check if license is valid
                //================================================================================
                if (!selectedVehicle.validLicense(customer.getLicense()))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);

                    builder.setTitle("Invalid License");
                    builder.setMessage("You don't have the necessary license for this vehicle");
                    builder.setCancelable(false);

                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog dialog = builder.create();

                    dialog.show();

                    return;
                }
            }
        }

        //Display InCityVehicleScreen
        startActivity(rentalIntent);
    }

    public void onTaxiClick(View view)
    {
        taxiIntent.putExtra("customer",customer);
        startActivity(taxiIntent);
    }
}


