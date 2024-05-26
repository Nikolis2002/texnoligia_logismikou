package com.ceid.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ceid.Network.ApiClient;
import com.ceid.Network.ApiService;
import com.ceid.Network.jsonStringParser;
import com.ceid.model.service.RentalService;
import com.ceid.model.transport.Rental;
import com.ceid.model.users.Customer;
import com.ceid.util.Coordinates;
import com.ceid.util.Map;
import com.ceid.util.MapWrapperReadyListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UnlockScreen extends AppCompatActivity implements MapWrapperReadyListener {

    private Map map;
    private Rental rental;
    private int serviceId;
    private Timer reservationTimer;
    private static final int CAMERA_REQUEST_CODE = 200;
    private ApiService api= ApiClient.getApiService();
    private Customer customer;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unlock_screen);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap);
        map = new Map(mapFragment, this, this);

        Intent data = getIntent();
        rental = (Rental)data.getSerializableExtra("vehicle");
        serviceId = data.getIntExtra("service_id", -1);
        customer= (Customer) data.getSerializableExtra("customer");


        reservationTimer = new Timer();

        reservationTimer.schedule(new TimerTask() {
            @Override
            public void run() {

                runOnUiThread(() -> {
                        Toast.makeText(getApplicationContext(), "Reservation time passed", Toast.LENGTH_SHORT).show();
                        cancelReservation();
                });
            }
        },5000);

    }

    public void openCamera(){
        IntentIntegrator qrScanner = new IntentIntegrator(UnlockScreen.this);
        qrScanner.setCaptureActivity(QrCamera.class);
        qrScanner.setOrientationLocked(false);
        qrScanner.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        qrScanner.setPrompt("Scan QR on vehicle");
        qrScanner.initiateScan();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {

    }

    public void cancelReservation(){
        List<java.util.Map<String,Object>> values = new ArrayList<>();
        java.util.Map<String, Object> cancelReservation = new LinkedHashMap<>();
        cancelReservation.put("id", serviceId);
        cancelReservation.put("vehicle",rental.getId());
        values.add(cancelReservation);

        String jsonString = jsonStringParser.createJsonString("cancelReservation",values);

        Call<ResponseBody> call = api.getFunction(jsonString);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                Intent intent = new Intent(UnlockScreen.this,MainScreen.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {

            }
        });
    }

    public void unlockVehicle(View view){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
                openCamera();




        }else {
            ActivityCompat.requestPermissions(UnlockScreen.this, new String[]{Manifest.permission.CAMERA},
                    CAMERA_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
               openCamera();
            }else{
                Toast.makeText(getApplicationContext(), "App do not have camera permission", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this,MainScreen.class);
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        IntentResult qrResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (qrResult != null) {
            String qr = qrResult.getContents();

            List<java.util.Map<String,Object>> values = new ArrayList<>();
            java.util.Map<String, Object> checkVehicle = new LinkedHashMap<>();
            checkVehicle.put("id", serviceId);
            checkVehicle.put("vehicle",Integer.parseInt(qr));
            values.add(checkVehicle);

            String jsonString = jsonStringParser.createJsonString("checkVehicleId",values);

            Call<ResponseBody> call = api.getFunction(jsonString);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    try {
                        boolean status = jsonStringParser.getbooleanFromJson(response);

                        if(status){

                            double balance= customer.getWallet().getBalance();

                            if(balance>10){
                                customer.getWallet().withdraw(10);
                                List<java.util.Map<String,Object>> values = new ArrayList<>();
                                java.util.Map<String, Object> unlockDate = new LinkedHashMap<>();
                                unlockDate.put("id", serviceId);
                                values.add(unlockDate);

                                String jsonString = jsonStringParser.createJsonString("unlockVehicle",values);

                                Call<ResponseBody> call_unlock = api.getFunction(jsonString);

                                call_unlock.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                                        List<java.util.Map<String,Object>> values = new ArrayList<>();
                                        java.util.Map<String, Object> creationDate = new LinkedHashMap<>();
                                        creationDate.put("service_id",serviceId);
                                        values.add(creationDate);

                                        String jsonString = jsonStringParser.createJsonString("checkTaxiComplete",values);
                                        Call<ResponseBody> call_date = api.getFunction(jsonString);

                                        call_date.enqueue(new Callback<ResponseBody>() {
                                            @Override
                                            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                                                if(response.isSuccessful()){

                                                    try {
                                                        ArrayList<String> responseArray = jsonStringParser.getResults(response);
                                                        LocalDateTime date = LocalDateTime.parse(responseArray.get(0));

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
                            }else{
                                Toast.makeText(getApplicationContext(), "You don't have the required amount in your wallet", Toast.LENGTH_SHORT).show();
                                Toast.makeText(getApplicationContext(), "Reservation canceled", Toast.LENGTH_SHORT).show();
                                cancelReservation();
                            }

                        }else{
                            Toast.makeText(getApplicationContext(), "This is not the vehicle you choose", Toast.LENGTH_SHORT).show();
                            cancelReservation();
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
        Intent intent = new Intent(this,MainScreen.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onMapWrapperReady()
    {
        Coordinates location = new Coordinates(rental.getTracker().getCoords().getLat(),rental.getTracker().getCoords().getLng());
        map.setZoom(15);
        map.setPosition(location);
        map.placePin(location,true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (reservationTimer != null) {
            reservationTimer.cancel();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (reservationTimer != null) {
            reservationTimer.cancel();
        }
    }
}
