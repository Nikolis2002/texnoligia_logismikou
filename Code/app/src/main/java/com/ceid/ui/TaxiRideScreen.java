package com.ceid.ui;

import java.util.concurrent.CountDownLatch;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Chronometer;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ceid.Network.ApiClient;
import com.ceid.Network.ApiService;
import com.ceid.Network.jsonStringParser;
import com.ceid.model.service.TaxiService;
import com.ceid.model.users.Customer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaxiRideScreen extends AppCompatActivity {

    private Chronometer timer;
    private Handler handler;
    private Runnable taxiRideCheck;
    private Runnable taxiPickUp;
    private TaxiService taxiService;
    private ApiService api= ApiClient.getApiService();
    private Customer customer;
    private double cost;
    private CountDownLatch waitCost;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taxi_ride_screen);
        Intent intent = getIntent();
        customer = (Customer)((App) getApplicationContext()).getUser();
        taxiService = (TaxiService) intent.getSerializableExtra("taxiService");
        CountDownLatch waitCost = new CountDownLatch(1);

        handler = new Handler();


        taxiPickUp = new Runnable() {
            @Override
            public void run() {

                isPickUp();

                handler.postDelayed(this, 2000);
            }
        };

        handler.postDelayed(taxiPickUp, 2000);



    }



    public void isPickUp() {
        List<Map<String,Object>> values = new ArrayList<>();
        java.util.Map<String, Object> taxiPickUpcheck = new LinkedHashMap<>();
        taxiPickUpcheck.put("request_id_in",taxiService.getId());
        values.add(taxiPickUpcheck);

        String jsonString = jsonStringParser.createJsonString("checkTaxiPickUp",values);
        Call<ResponseBody> call = api.getFunction(jsonString);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                if(response.isSuccessful()){

                    try {
                        boolean status = jsonStringParser.getbooleanFromJson(response);

                        if(status){
                            timer = findViewById(R.id.timer);
                            timer.start();
                            handler.removeCallbacks(taxiPickUp);

                            taxiRideCheck = new Runnable() {
                                @Override
                                public void run() {
                                    rideStatus();
                                    handler.postDelayed(this, 2000);
                                }
                            };

                            handler.postDelayed(taxiRideCheck, 2000);

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
    }

    public void rideStatus(){
        List<Map<String,Object>> values = new ArrayList<>();
        java.util.Map<String, Object> taxiComplete = new LinkedHashMap<>();
        taxiComplete.put("service_id",taxiService.getId());
        values.add(taxiComplete);

        String jsonString = jsonStringParser.createJsonString("checkTaxiComplete",values);
        Call<ResponseBody> call = api.getFunction(jsonString);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                if(response.isSuccessful()){

                    try {
                        boolean status = jsonStringParser.getbooleanFromJson(response);

                        if(status){
                            handler.removeCallbacks(taxiRideCheck);

                            rideCost();


                            String payment = String.valueOf(taxiService.getPayment().getMethod());

                            if(payment.equals("CASH")){
                                Intent intent = new Intent(TaxiRideScreen.this, MainScreen.class);
                                startActivity(intent);
                                finish();
                            }else{
                                double balance = customer.getWallet().getBalance();
                                if(balance>cost){
                                    customer.getWallet().withdraw(cost);
                                    int points=customer.getPoints().calcPoints(cost);
                                    int new_points = points + customer.getPoints().getPoints();

                                    List<Map<String,Object>> values = new ArrayList<>();
                                    java.util.Map<String, Object> updatePoints = new LinkedHashMap<>();
                                    updatePoints.put("service_id",taxiService.getId());
                                    updatePoints.put("points",points);
                                    updatePoints.put("username",customer.getUsername());
                                    updatePoints.put("newpoints",new_points);
                                    values.add(updatePoints);

                                    String jsonString = jsonStringParser.createJsonString("updatePoints",values);
                                    Call<ResponseBody> call_points = api.getFunction(jsonString);

                                    call_points.enqueue(new Callback<ResponseBody>() {
                                        @Override
                                        public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                                            Intent intent = new Intent(TaxiRideScreen.this, MainScreen.class);
                                            startActivity(intent);
                                            finish();
                                        }

                                        @Override
                                        public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {

                                        }
                                    });

                                }else{
                                    customer.getWallet().withdraw(cost);
                                    Toast.makeText(getApplicationContext(), "Your wallet balance is negative", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(TaxiRideScreen.this,MainScreen.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }

                    } catch (IOException | InterruptedException e) {
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

    public void rideCost() throws InterruptedException {
        List<Map<String,Object>> values = new ArrayList<>();
        Map<String, Object> paymentCheck = new LinkedHashMap<>();
        paymentCheck.put("service_id",taxiService.getId());
        values.add(paymentCheck);
        String jsonString = jsonStringParser.createJsonString("getPayment",values);
        waitCost.await();
        Call<ResponseBody> call = api.getFunction(jsonString);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    try {
                        ArrayList<String> responseArray = jsonStringParser.getResults(response);
                        cost = Double.parseDouble(responseArray.get(0));
                        waitCost.countDown();

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }


                }else{
                    System.out.println("Error message");
                    waitCost.countDown();

                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {

            }
        });


    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(taxiPickUp);
        handler.removeCallbacks(taxiRideCheck);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(taxiPickUp);
        handler.removeCallbacks(taxiRideCheck);
    }

}
