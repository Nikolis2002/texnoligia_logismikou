package com.ceid.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ceid.Network.ApiClient;
import com.ceid.Network.ApiService;
import com.ceid.Network.jsonStringParser;
import com.ceid.model.payment_methods.Payment;
import com.ceid.model.service.TaxiRequest;
import com.ceid.model.users.TaxiDriver;
import com.ceid.model.users.User;
import com.ceid.util.Map;
import com.ceid.util.MapWrapperReadyListener;
import com.ceid.util.Timer;
import com.google.android.gms.maps.SupportMapFragment;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaxiTransportScreen extends AppCompatActivity implements MapWrapperReadyListener {

    private Map map;
    private Button button;
    private Timer timer = new Timer();
    private Instant startTimer;
    private Instant stopTimer;
    private TaxiRequest taxiRequest;
    private TaxiDriver taxiDriver;
    private ApiService api= ApiClient.getApiService();
    private boolean status;
    private long elapsedTime;
    private double costCalc;

    protected  void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.taxi_transport_screen);

        OnBackPressedDispatcher dispatcher = getOnBackPressedDispatcher();
        dispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                //do nothing stay on the same screen
            }
        });

       Intent taxiRequestData = getIntent();
       taxiRequest = (TaxiRequest) taxiRequestData.getSerializableExtra("taxiRequest");
       taxiDriver = (TaxiDriver) User.getCurrentUser();

       button = findViewById(R.id.startEndRouteButton);
       SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.locationMapView);
       map = new Map(mapFragment, this, this);
       showEta();
       button.setOnClickListener(v -> startRoute());
   }

    @Override
    public void onMapWrapperReady() {
        map.placePin(taxiRequest.getPickupLocation(),true,R.drawable.emoji_people);
        map.placePin(taxiDriver.getTaxi().getCoords(), false,R.drawable.taxi_image);
        map.setZoom(14);
        map.setPosition(taxiRequest.getPickupLocation());
    }

    public void startRoute(){
        checkRequest();
    }

    public void checkRequest(){
        List<java.util.Map<String,Object>> values = new ArrayList<>();
        java.util.Map<String, Object> taxiReservationCheck = new LinkedHashMap<>();
        taxiReservationCheck.put("request_id_in",taxiRequest.getId());
        values.add(taxiReservationCheck);

        String jsonString = jsonStringParser.createJsonString("checkTaxiReservationSecond",values);
        Call<ResponseBody> call = api.callProcedure(jsonString);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                if(response.isSuccessful()){
                    try {
                        status = jsonStringParser.getbooleanFromJson(response);

                        if(status) {

                            List<java.util.Map<String,Object>> values = new ArrayList<>();
                            java.util.Map<String, Object> updatePickUpRequest = new LinkedHashMap<>();
                            updatePickUpRequest.put("updatePickUpRequest", taxiRequest.getId());
                            values.add(updatePickUpRequest);

                            String jsonString = jsonStringParser.createJsonString("updatePickUpRequest",values);

                            Call<ResponseBody> call_update = api.callProcedure(jsonString);

                            call_update.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                                    if(response.isSuccessful()){

                                        map.setZoom(13);
                                        map.setPosition(taxiRequest.getDestination());
                                        map.placePin(taxiRequest.getDestination(),true);
                                        String text = "End Route";
                                        startTimer = timer.startTimer();
                                        button.setText(text);
                                        View.OnClickListener endRouteListener = v -> endRoute();
                                        button.setOnClickListener(endRouteListener);
                                    }else{
                                        System.out.println("Error message");
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {

                                }
                            });


                        }else{
                            Toast.makeText(TaxiTransportScreen.this,"The request is not available", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(TaxiTransportScreen.this,MainScreenTaxi.class);
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
    }

    public void showEta(){
       String time = taxiRequest.calculateEta(taxiDriver.getTaxi().getCoords());
       TextView etaTextview=findViewById(R.id.eta);
       String msg="Eta: " + time +" min";
       etaTextview.setText(msg);
    }


    public void endRoute(){
        stopTimer=timer.stopTimer();
        calculateCost();
        Payment.Method payment = taxiRequest.getPaymentMethod();
        String paymentString=String.valueOf(payment);
        List<java.util.Map<String,Object>> values = new ArrayList<>();
        java.util.Map<String, Object> taxiReservationComplete = new LinkedHashMap<>();
        taxiReservationComplete.put("id",taxiRequest.getId());
        taxiReservationComplete.put("method",taxiRequest.getPaymentMethod());
        taxiReservationComplete.put("value",String.format("%.2f", costCalc));
        values.add(taxiReservationComplete);

        String jsonString = jsonStringParser.createJsonString("completeTaxiRequest",values);

        Call<ResponseBody> call = api.callProcedure(jsonString);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                if(response.isSuccessful()){

                    if(paymentString.equals("WALLET")){
                        String cost=String.format("%.2f", costCalc);
                        taxiDriver.getWallet().addToWallet(Double.parseDouble(cost));

                        List<java.util.Map<String,Object>> values = new ArrayList<>();
                        java.util.Map<String, Object> updateWallet = new LinkedHashMap<>();
                        updateWallet.put("username", taxiDriver.getUsername());
                        updateWallet.put("balance", taxiDriver.getWallet().getBalance());
                        values.add(updateWallet);

                        String jsonString = jsonStringParser.createJsonString("updateWallet",values);

                        Call<ResponseBody> call_wallet = api.callProcedure(jsonString);

                        call_wallet.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                                Intent intent = new Intent(TaxiTransportScreen.this,MainScreenTaxi.class);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {

                            }
                        });

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

    public long getTime(){
        return timer.elapsedTime(startTimer,stopTimer);
    }

    public void calculateCost(){
        elapsedTime=getTime();
        double cost= elapsedTime*0.012;
        cost += 1.5;

        costCalc=cost;
    }
}
