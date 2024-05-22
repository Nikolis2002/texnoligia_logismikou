package com.ceid.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.ceid.Network.ApiClient;
import com.ceid.Network.ApiService;
import com.ceid.Network.jsonStringParser;
import com.ceid.model.users.Customer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaxiRequestWaitScreen extends AppCompatActivity {

    private Handler handler;
    private boolean status=false;
    Timer reservationTimer;
    private int serviceId;
    ApiService api= ApiClient.getApiService();
    Customer customer;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taxi_request_wait_screen);

        Intent intent = getIntent();
        serviceId = intent.getIntExtra("serviceId",0);
        customer= (Customer) intent.getSerializableExtra("customer");

        handler = new Handler();
        rideStatus();

        reservationTimer = new Timer();

        reservationTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                cancelTaxiReservation();
                handler.removeCallbacks(taxiFoundCheck);

                runOnUiThread(() -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(TaxiRequestWaitScreen.this);
                    builder.setMessage("Select a option");
                    builder.setTitle("Taxi was not found");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Resend", (alertDialog, which) -> {
                        resumeTaxiReservation();
                        Intent intent = new Intent(TaxiRequestWaitScreen.this, TaxiRequestWaitScreen.class);
                        intent.putExtra("serviceId",serviceId);
                        intent.putExtra("customer",customer);
                        startActivity(intent);
                        finish();
                    });
                    builder.setNegativeButton("Cancel", (alertDialog, which) -> {
                        Intent intent = new Intent(TaxiRequestWaitScreen.this, InCityScreen.class);
                        intent.putExtra("customer",customer);
                        startActivity(intent);
                        finish();
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                });
            }
        },20000);
    }

    Runnable taxiFoundCheck = new Runnable() {
        @Override
        public void run() {

            List<Map<String,Object>> values = new ArrayList<>();
            Map<String, Object> taxiReservationCheck = new LinkedHashMap<>();
            taxiReservationCheck.put("checkTaxiReservation",serviceId);
            values.add(taxiReservationCheck);

            String jsonString = jsonStringParser.createJsonString("checkTaxiReservation",values);

            Call<ResponseBody> call = api.getFunction(jsonString);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                    if(response.isSuccessful()){
                        try {
                            status = jsonStringParser.getbooleanFromJson(response);
                            Toast.makeText(getApplicationContext(), String.valueOf(status), Toast.LENGTH_SHORT).show();

                            if(status) {
                                Intent intent = new Intent(TaxiRequestWaitScreen.this, TaxiRideScreen.class);
                                startActivity(intent);
                            }
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
            handler.postDelayed(this, 2000);
        }
    };

    public void cancelTaxiReservation(){

        List<Map<String,Object>> values = new ArrayList<>();
        Map<String, Object> taxiReservationCancel = new LinkedHashMap<>();
        taxiReservationCancel.put("cancelService",serviceId);
        values.add(taxiReservationCancel);

        String jsonString = jsonStringParser.createJsonString("cancelService",values);

        Call<ResponseBody> call = api.getFunction(jsonString);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                if(response.isSuccessful()){


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

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {

    }

    public void resumeTaxiReservation(){

        List<Map<String,Object>> values = new ArrayList<>();
        Map<String, Object> taxiReservationCancel = new LinkedHashMap<>();
        taxiReservationCancel.put("resumeService",serviceId);
        values.add(taxiReservationCancel);

        String jsonString = jsonStringParser.createJsonString("resumeService",values);

        Call<ResponseBody> call = api.getFunction(jsonString);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                if(response.isSuccessful()){


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

    public void rideStatus() {
        handler.post(taxiFoundCheck);
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(taxiFoundCheck);
        reservationTimer.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(taxiFoundCheck);
        reservationTimer.cancel();
    }
}
