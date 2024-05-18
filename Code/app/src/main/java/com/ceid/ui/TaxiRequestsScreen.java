package com.ceid.ui;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ceid.model.service.TaxiRequest;
import com.ceid.util.Location;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TaxiRequestsScreen extends AppCompatActivity {
    private RecyclerView requestView;
    private TaxiRequestAdapter requestAdapter;
    private Location location;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taxi_request_screen);



    }

    public void taxiRequestSelect(View view){

        requestView = findViewById(R.id.requestView);
        requestView.setLayoutManager(new LinearLayoutManager(this));
        List<String> taxiRequestList = Arrays.asList("test","test2","test3","test4","test5","test6","test7","test8","test9");
        requestAdapter = new TaxiRequestAdapter(taxiRequestList);
        requestView.setAdapter(requestAdapter);
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


}
