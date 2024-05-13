package com.ceid.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.ceid.util.Coordinates;
import com.ceid.util.Location;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TaxiSelect extends AppCompatActivity implements ActivityResultCallback<ActivityResult>{
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private Bundle destinationScreenData;
    private Location location;
    private Coordinates destinationCoord;
    private float zoom;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.taxi_select);

        gpsLocation();

        this.activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),this
        );

    }


    public void findTaxiButton(View view){
        boolean fieldCheck=checkLocField();

        if(fieldCheck){
            Toast.makeText(getApplicationContext(), "Set a destination!", Toast.LENGTH_SHORT).show();
        }

        if(paymentCheck()==-1){
            Toast.makeText(getApplicationContext(), "Select a payment method!", Toast.LENGTH_SHORT).show();
        }

    }

    public void insertDestination(View view){
        Intent destinationIntent = new Intent(TaxiSelect.this, LocationScreen.class);
        destinationScreenData = new Bundle();
        destinationScreenData.putSerializable("coords",destinationCoord);
        destinationScreenData.putString("text","Choose your Destination");
        destinationScreenData.putFloat("zoom",zoom);
        destinationScreenData.putSerializable("location",location);
        destinationIntent.putExtras(destinationScreenData);
        activityResultLauncher.launch(destinationIntent);
    }

    private int paymentCheck(){
        RadioGroup radioGroup = findViewById(R.id.paymentRadioGroup);

        return radioGroup.getCheckedRadioButtonId();
    }

    private void enableTaxiBtn(Boolean action){
        Button button = findViewById(R.id.findCabButton);
        button.setEnabled(!action);
    }

    private boolean checkLocField(){
        TextInputEditText destCoords = findViewById(R.id.endPointInput);
        String end = destCoords.getText().toString();

        return end.isEmpty();
    }

    private void gpsLocation(){

        List<Location> locationList= new ArrayList<>();

        locationList.add(new Location(
                38.24581944113243,
                21.735667393600977,
                "Πλ. Βασιλέως Γεωργίου Α"
        ));

        locationList.add(new Location(
                38.26156326500886,
                21.744230924244444,
                "Στάδιο Παναχαϊκής Κώστας Δαβουρλής"
        ));

        locationList.add(new Location(
                38.23805202046475,
                21.72593019402509,
                "Veso Mare"
        ));

        locationList.add(new Location(
                38.28642678834642,
                21.78648559358893,
                "Πανεπιστημιούπολη Πατρών"
        ));

        locationList.add(new Location(
                38.221008554956555,
                21.741725145302286,
                "Ακρωτηρίου 128-132"
        ));

        locationList.add(new Location(
                38.23119708759673,
                21.763954185920586,
                "Ηρακλέους 13"
        ));

        Random random = new Random();
        int randomLocation = random.nextInt(locationList.size());
        location = locationList.get(randomLocation);
        TextInputEditText fromInput = findViewById(R.id.startPointInput);
        fromInput.setText(location.getAddress());
    }


    @Override
    public void onActivityResult(ActivityResult result) {
        if (result.getResultCode() == Activity.RESULT_OK){
            Intent data = result.getData();
            assert data != null;
            destinationScreenData = data.getExtras();

            assert destinationScreenData != null;
            destinationCoord = (Coordinates) destinationScreenData.getSerializable("coords");
            zoom = destinationScreenData.getFloat("zoom");

            if (destinationCoord != null){
                TextInputEditText destCoords = findViewById(R.id.endPointInput);
                destCoords.setText(destinationCoord.toString());
                double taxiCost=location.estimateTaxiCost(destinationCoord);
                TextView estimateCost = findViewById(R.id.estimatedCost);
                String taxiCostFormatted= new DecimalFormat ("#.00").format(taxiCost);
                String taxiCostFormat = taxiCostFormatted + "€";
                estimateCost.setText(taxiCostFormat);
                TextView finalCostText = findViewById(R.id.finalCost);
                double finalCostEstimated = taxiCost + 1.5;
                String finaCostEstimatedString = new DecimalFormat ("#.00").format(finalCostEstimated);
                String finalCost = finaCostEstimatedString + "€";
                finalCostText.setText(finalCost);
            }


            boolean fieldCheck=checkLocField();
            enableTaxiBtn(fieldCheck);

            if(fieldCheck){
                Toast.makeText(getApplicationContext(), "Set a destination", Toast.LENGTH_SHORT).show();
            }

         }else{
            Toast.makeText(getApplicationContext(), "Set a destination", Toast.LENGTH_SHORT).show();
            boolean fieldCheck=checkLocField();
            enableTaxiBtn(fieldCheck);
        }
    }
}
