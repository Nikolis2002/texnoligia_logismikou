package com.ceid.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.ceid.model.service.RentalService;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Arrays;

public class endRide extends AppCompatActivity {


    private Bundle bundle;
    private String time;
    private RentalService service;
    private TextView duration,cost,points;

    private Button photoButton1,photoButton2,photoButton3,photobutton4;
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
    public void attachPhoto(View view)
    {
        pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
    }
    public void saveImage(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            if (inputStream == null) {
                Log.e("PhotoPicker", "InputStream is null for URI: " + uri);
                return;
            }
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            //bArray1 = bos.toByteArray();
           // System.out.println(Arrays.toString(bArray));
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PhotoPicker", "Error saving image", e);
        }
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Toast.makeText(getApplicationContext(), "You can't go back here!",
                Toast.LENGTH_LONG).show();
    }

}


