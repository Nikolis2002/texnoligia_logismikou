package com.ceid.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.ceid.model.service.RentalService;

import java.io.Serializable;

public class endRide extends AppCompatActivity {
    ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                // Callback is invoked after the user selects a media item or closes the
                // photo picker.
                if (uri != null) {
                    //saveImage(uri);
                } else {
                    Toast.makeText(getApplicationContext(), "No media selected!",
                            Toast.LENGTH_LONG).show();
                }
            });
    private Bundle bundle;
    private String time;
    private RentalService service;
    private TextView duration,cost,points;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.end_ride_screen);
        bundle= getIntent().getExtras();
        time=getIntent().getStringExtra("timestring");
        service= (RentalService) bundle.getSerializable("service");
        duration=findViewById(R.id.time);
        cost=findViewById(R.id.cost);
        points=findViewById(R.id.points);
        duration.setText(time);
        points.setText(String.valueOf(service.getPoints()));


    }
    public void attachPhoto(View view)
    {
        pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Toast.makeText(getApplicationContext(), "You can't go back here!",
                Toast.LENGTH_LONG).show();
    }

}


