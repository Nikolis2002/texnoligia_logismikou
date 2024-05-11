package com.ceid.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.ceid.util.Coordinates;
import com.ceid.util.Location;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TaxiSelect extends AppCompatActivity implements ActivityResultCallback<ActivityResult> {
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private Bundle destinationScreenData = null;
    private Intent destinationIntent;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.taxi_select);

        selectLocation();

        Button locButton = findViewById(R.id.LocateButton);

        locButton.setOnClickListener(view -> selectLocation());

        this.activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),this
        );

        destinationIntent = new Intent(this, LocationScreen.class);

        buttonEnableCheck();

    }

    public void onClick(View view){

        if (destinationScreenData != null) {
            destinationIntent.putExtras(destinationScreenData);
        }

        activityResultLauncher.launch(destinationIntent);
    }

    private void buttonEnableCheck(){
        Button button = findViewById(R.id.findCabButton);
        TextInputEditText destCoords = findViewById(R.id.endPointInput);
        String end = destCoords.getText().toString();

        if(end.isEmpty()){
            button.setEnabled(false);
        }else{
            button.setEnabled(true);
        }

    }

    private void selectLocation(){

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

        Location location = locationList.get(randomLocation);
        String locationName=location.getAddress();
        TextInputEditText fromInput = findViewById(R.id.startPointInput);
        fromInput.setText(locationName);
    }


    @Override
    public void onActivityResult(ActivityResult result) {
        if (result.getResultCode() == Activity.RESULT_OK){
            Intent data = result.getData();
            destinationScreenData = data.getExtras();

            Coordinates selectedCoord = (Coordinates) destinationScreenData.getSerializable("coords");

            if (selectedCoord != null){
                TextInputEditText destCoords = findViewById(R.id.endPointInput);
                destCoords.setText(selectedCoord.toString());
            }

            buttonEnableCheck();
         }
    }
}
