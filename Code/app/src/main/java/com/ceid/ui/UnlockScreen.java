package com.ceid.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ceid.Network.ApiClient;
import com.ceid.Network.ApiService;
import com.ceid.Network.jsonStringParser;
import com.ceid.model.service.RentalService;
import com.ceid.model.transport.Bicycle;
import com.ceid.model.transport.CityCar;
import com.ceid.model.transport.Motorcycle;
import com.ceid.model.transport.Rental;
import com.ceid.model.users.Customer;
import com.ceid.model.users.User;
import com.ceid.util.Coordinates;
import com.ceid.util.DateFormat;
import com.ceid.util.Map;
import com.ceid.util.MapWrapperReadyListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UnlockScreen extends AppCompatActivity implements MapWrapperReadyListener {

    private Map reserveVehMap;
    private Rental rental;
    private int serviceId;
    private CountDownTimer reservationTimer;
    private static final int CAMERA_REQUEST_CODE = 200;
    private ApiService api= ApiClient.getApiService();
    private Customer customer;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unlock_screen);

        //Disable back button in this screen
        //=================================================================================
        OnBackPressedDispatcher dispatcher = getOnBackPressedDispatcher();
        dispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                //do nothing stay on the same screen
            }
        });

        //Setup map
        //=================================================================================
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap);
        reserveVehMap = new Map(mapFragment, this, this);

        //Get data from previous screen
        //=================================================================================
        Intent data = getIntent();
        rental = (Rental)data.getSerializableExtra("vehicle");
        serviceId = data.getIntExtra("service_id", -1);
        customer  = (Customer)User.getCurrentUser();

        //Start a timer for the reservation time
        //=================================================================================

        TextView timerTextView = findViewById(R.id.countdown);

        reservationTimer = new CountDownTimer(40000, 1000)
        {
            @Override
            public void onTick(long millisUntilFinished) {
                // Update TextView with the remaining time
                timerTextView.setText("Reserved for " + DateFormat.millisToTimeString(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                timerTextView.setText("Reserved for 00:00");

                Toast.makeText(getApplicationContext(), "Reservation time passed", Toast.LENGTH_SHORT).show();
                cancelReservation();
                Intent intent = new Intent(UnlockScreen.this,MainScreen.class);
                startActivity(intent);
                finish();
            }
        };

        reservationTimer.start();
    }

    //Got the map from the map service
    //Display the map, along with vehicle's location
    //============================================================================================
    @Override
    public void onMapWrapperReady()
    {
        Coordinates location = new Coordinates(rental.getTracker().getCoords().getLat(),rental.getTracker().getCoords().getLng());
        reserveVehMap.setZoom(15);
        reserveVehMap.setPosition(location);

        //Place pin according to the vehicle's type
        //================================================================================
        if (rental instanceof CityCar)
            reserveVehMap.placePin(location,true, R.drawable.in_city_car);
        else if (rental instanceof Motorcycle)
            reserveVehMap.placePin(location,true, R.drawable.in_city_motorcycle);
        else if (rental instanceof Bicycle)
            reserveVehMap.placePin(location,true, R.drawable.in_city_bicycle);
        else
            reserveVehMap.placePin(location,true, R.drawable.in_city_scooter);

    }

    public void openCamera(){
        IntentIntegrator qrScanner = new IntentIntegrator(UnlockScreen.this);
        qrScanner.setCaptureActivity(QrCamera.class);
        qrScanner.setOrientationLocked(false);
        qrScanner.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        qrScanner.setPrompt("Scan QR on vehicle");
        qrScanner.initiateScan();
    }


    //User cancels reservation
    //============================================================================================
    public void cancelReservation(){
        List<java.util.Map<String,Object>> values = new ArrayList<>();
        java.util.Map<String, Object> cancelReservation = new LinkedHashMap<>();
        cancelReservation.put("id", serviceId);
        cancelReservation.put("vehicle",rental.getId());
        values.add(cancelReservation);

        String jsonString = jsonStringParser.createJsonString("cancelReservation",values);

        Call<ResponseBody> call = api.callProcedure(jsonString);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {


            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {

            }
        });
    }

    //Pressing the unlock button
    //============================================================================================
    public void unlockVehicle(View view){

        //Check if app has camera permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
                openCamera();

        }else //Ask camera permission
        {
            ActivityCompat.requestPermissions(UnlockScreen.this, new String[]{Manifest.permission.CAMERA},
                    CAMERA_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST_CODE)
        {
            //User granted permission
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
               openCamera();
            }
            //User denied permission
            else
            {
                //deniedPermission
                Toast.makeText(getApplicationContext(), "App does not have camera permission", Toast.LENGTH_SHORT).show();
                cancelReservation();
                Intent intent = new Intent(UnlockScreen.this,MainScreen.class);
                startActivity(intent);
                finish();
            }
        }
    }

    //QR scan results
    //=======================================================================
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        IntentResult qrResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (qrResult != null & data!=null) {
            String qr = qrResult.getContents();
            qr=qr.trim();

            //Prepare data for the database
            //We need to check if the QR we scanned on the vehicle corresponds to the vehicle reserved in this service
            //==============================================================================================
            List<java.util.Map<String,Object>> values = new ArrayList<>();
            java.util.Map<String, Object> checkVehicle = new LinkedHashMap<>();
            checkVehicle.put("id", serviceId);
            checkVehicle.put("vehicle",Integer.parseInt(qr));
            values.add(checkVehicle);

            String jsonString = jsonStringParser.createJsonString("checkVehicleId",values);

            Call<ResponseBody> call = api.callProcedure(jsonString);

            //Make the database call
            //==============================================================================================
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    try {
                        boolean status = jsonStringParser.getbooleanFromJson(response);

                        //Customer reserved vehicle
                        //==============================================================================================
                        if(status){

                            double balance= customer.getWallet().getBalance();

                            //Check if customer has sufficient funds for unlocking the vehicle
                            if(balance >= 1){

                                //Remove a small amount from customer's wallet for unlocking the vehicle
                                customer.getWallet().withdraw(1);

                                //Prepare database data
                                //=========================================================================
                                List<java.util.Map<String,Object>> values = new ArrayList<>();
                                java.util.Map<String, Object> unlockDate = new LinkedHashMap<>();
                                unlockDate.put("id", serviceId);
                                unlockDate.put("name", customer.getUsername());
                                values.add(unlockDate);

                                String jsonString = jsonStringParser.createJsonString("unlockVehicle",values);

                                Call<ResponseBody> call_unlock = api.callProcedure(jsonString);

                                //Update database with the unlock event
                                //=========================================================================
                                call_unlock.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                                        //Success message
                                        Toast.makeText(getApplicationContext(), "Vehicle unlocked successfully", Toast.LENGTH_SHORT).show();

                                        //Prepare another call to the database, to get creation date of the service
                                        //=========================================================================
                                        List<java.util.Map<String,Object>> values = new ArrayList<>();
                                        java.util.Map<String, Object> creationDate = new LinkedHashMap<>();
                                        creationDate.put("service_id",serviceId);
                                        values.add(creationDate);

                                        String jsonString = jsonStringParser.createJsonString("rentalService",values);
                                        Call<ResponseBody> call_date = api.callProcedure(jsonString);

                                        //Make the call
                                        //=========================================================================
                                        call_date.enqueue(new Callback<ResponseBody>() {
                                            @Override
                                            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                                                if(response.isSuccessful()){

                                                    try {
                                                        ArrayList<String> responseArray = jsonStringParser.getResults(response);

                                                        Instant instant = Instant.parse(responseArray.get(0));
                                                        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
                                                        LocalDateTime date = zonedDateTime.toLocalDateTime();

                                                        //Create the service object
                                                        //Pass this to the next screen
                                                        //=========================================================================
                                                        RentalService rentalService = new RentalService(
                                                                serviceId,
                                                                date,
                                                                null,
                                                                null,
                                                                0,
                                                                rental
                                                        );

                                                        Intent intent = new Intent(UnlockScreen.this,TransportScreen.class);
                                                        intent.putExtra("service", rentalService);
                                                        startActivity(intent);
                                                        finish();

                                                    } catch (IOException e) {
                                                        throw new RuntimeException(e);
                                                    }

                                                }else{
                                                    System.out.println("Error message");
                                                }

                                            }
                                            @Override
                                            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {
                                                System.out.println("Error message");
                                            }
                                        });
                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {

                                    }
                                });
                            }
                            //Customer did not have sufficient funds for the unlocking
                            //Cancel reservation
                            //=============================================================================
                            else
                            {
                                Toast.makeText(getApplicationContext(), "You don't have the required amount in your wallet", Toast.LENGTH_SHORT).show();
                                cancelReservation();
                                Intent intent = new Intent(UnlockScreen.this,MainScreen.class);
                                startActivity(intent);
                                finish();
                            }

                        }
                        //Customer has not reserved scanned vehicle
                        //==============================================================================================
                        else
                        {
                            Toast.makeText(getApplicationContext(), "This is not the vehicle you chose", Toast.LENGTH_SHORT).show();
                        }


                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {

                }
            });
        }
    }

    public void cancelReservation(View view){
        cancelReservation();
        Intent intent = new Intent(UnlockScreen.this,MainScreen.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (reservationTimer != null) {
            reservationTimer.cancel();
        }
    }
}
