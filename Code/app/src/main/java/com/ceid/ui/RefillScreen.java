package com.ceid.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.ceid.Network.ApiClient;
import com.ceid.Network.ApiService;
import com.ceid.Network.PostHelper;
import com.ceid.model.service.GasStation;
import com.ceid.model.service.Refill;
import com.ceid.model.transport.CityCar;
import com.ceid.model.transport.ElectricScooter;
import com.ceid.model.transport.Motorcycle;
import com.ceid.model.transport.Rental;
import com.ceid.model.transport.SpecializedTracker;
import com.ceid.model.transport.Tracker;
import com.ceid.model.transport.VehicleTracker;
import com.ceid.model.users.Customer;
import com.ceid.util.Coordinates;
import com.ceid.util.GenericCallback;
import com.ceid.util.Map;
import com.ceid.util.MapWrapperReadyListener;
import com.ceid.util.PositiveInteger;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.material.textfield.TextInputEditText;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;

public class RefillScreen extends AppCompatActivity implements MapWrapperReadyListener, GoogleMap.OnMapClickListener{

    private Map map;
    private Chronometer timer;
    private double addNumber=20;
    Marker carMarker=null;
    private int[] disasterCounter= new int[2];


    ArrayList<GasStation> gasStationList=null;

    int id;

    Rental car = new CityCar(
            "ABC-1234",
            true,
            0,
            "MONDEO",
            "FORD",
            "1993",
            1.40,
            new Coordinates(38.2442870,21.7326153),
            new PositiveInteger(100)
    );
    String trackerType;
    //Customer customer =(Customer)((App) getApplicationContext()).getUser();
    private final ApiService api= ApiClient.getApiService();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.refill);
        disasterCounter[0]=disasterCounter[1]=0;
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.locationMapViewRefill);

        map = new Map(mapFragment, this,this);
        map.setClickable(true);
        map.setClickListener(this);
        timer = findViewById(R.id.timer2);
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();
    }


    @Override
    public void onMapWrapperReady() {
        //map.getMap().getUiSettings().setAllGesturesEnabled(false);
        Coordinates Patra = new Coordinates( 38.246639, 21.734573);
        this.map.setZoom(12);
        this.map.setPosition(Patra);


        if(car instanceof CityCar){
            id=R.drawable.in_city_car;
        }
        else if(car instanceof Motorcycle){
            id=R.drawable.in_city_motorcycle;
        } else if (car instanceof ElectricScooter) {
            id=R.drawable.electric_scooter_fill0_wght400_grad0_opsz24;
        }
        else{
            id=R.drawable.in_city_bicycle;
        }

        carMarker= map.placePin(car.getTracker().getCoords(), false,id);
        carMarker.setTag(car);



        if(car.checksGas()) {
            trackerType="specialized";
            PostHelper.getGasStations(api, "gas_station", new GenericCallback<ArrayList<GasStation>>() {
                @Override
                public void onSuccess(ArrayList<GasStation> data) {

                    for (GasStation station : data) {
                        Marker marker = map.placePin(station.getCoords(), false);
                        marker.setTag(station);
                    }


                    SpecializedTracker tracker = null;
                }

                @Override
                public void onFailure(Exception e) {
                    Log.d("fail", "fail");
                }

            });
        }
        else{
            trackerType="vechicleTracker";
            TextView textView=findViewById(R.id.textView23);
            textView.setText("Your Location:");

            View view=findViewById(R.id.button7);
            view.setVisibility(View.GONE);
        }

    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {

            if(carMarker!=null)
            {
                PostHelper.getTrackerOfRental(api,trackerType, String.valueOf(addNumber),new GenericCallback<VehicleTracker>() {

                    @Override
                    public void onSuccess(VehicleTracker data) {
                        TextInputEditText dista=findViewById(R.id.distaRefill);
                        addNumber=data.getDistanceTraveled();
                        dista.setText("Distance Travelled: "+ String.valueOf(addNumber));
                    }

                    @Override
                    public void onFailure(Exception e) {
                        if(disasterCounter[0]==0||disasterCounter[0]>100) {
                            showAlert("Cant retrieve vehicle location");
                        }
                        if(disasterCounter[0]==100)
                            disasterCounter[0]=0;

                        disasterCounter[0]++;
                    }
                });
            }
        carMarker.remove();
        Coordinates coords=new Coordinates(latLng);
        car.getTracker().setCoords(coords);
        carMarker=map.placePin(coords,false,id);


    }

    public void refillBtn(View view){

        PostHelper.getTrackerOfRental(api,trackerType, String.valueOf(0),new GenericCallback<VehicleTracker>() {

            @Override
            public void onSuccess(VehicleTracker data) {
                GasStation minGasStation=sortArrayList(car.getTracker().getCoords(), gasStationList);

                View popupView = LayoutInflater.from(RefillScreen.this).inflate(R.layout.refill_popup, null);

                // Create the popup window
                PopupWindow popupWindow = new PopupWindow(
                        popupView,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                );

                // Show the popup window
                popupWindow.showAtLocation(
                        findViewById(android.R.id.content),
                        Gravity.CENTER,
                        0,
                        0
                );
                SpecializedTracker initTracker= (SpecializedTracker) data;

                Button cancel = popupView.findViewById(R.id.cancel2);
                Button complete = popupView.findViewById(R.id.complete);

                complete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {

                        callback(popupWindow,initTracker,minGasStation);

                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        cancelRefill(popupWindow,initTracker,minGasStation);
                    }
                });


            }

            @Override
            public void onFailure(Exception e) {
                showAlert("Could not retrieve gas level");
            }
        });
    }

    public GasStation sortArrayList(Coordinates coords, ArrayList<GasStation> list){
        GasStation minGasStation=list.get(0);
        for(GasStation gasStation:list){
            if(coords.distance(gasStation.getCoords())<coords.distance(minGasStation.getCoords())){
                minGasStation=gasStation;
            }
        }
        return minGasStation;
    }

    public void showAlert(String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(RefillScreen.this);

        builder.setTitle("CONNECTION error");
        builder.setMessage(msg);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();

            }
        });

        builder.create().show();
    }

    public void callback(PopupWindow window,SpecializedTracker initTracker,GasStation station){
        PostHelper.getTrackerOfRental(api,trackerType, String.valueOf(0),new GenericCallback<VehicleTracker>() {
            @Override
            public void onSuccess(VehicleTracker data) {
                SpecializedTracker  finalTracker= (SpecializedTracker) data;

                int diff=finalTracker.getGas().posDiff(initTracker.getGas());

                Refill refill=new Refill(LocalDateTime.now(),
                        station,
                        initTracker.getGas(),
                        finalTracker.getGas());
            }

            @Override
            public void onFailure(Exception e) {
                if(disasterCounter[1]<100) {
                    callback(window,initTracker,station);
                }
                if(disasterCounter[1]==100) {
                    cancelRefill(window,initTracker,station);
                    disasterCounter[1] = 0;
                }
                disasterCounter[1]++;
            }
        });
    }

    public void cancelRefill(PopupWindow window,SpecializedTracker tracker,GasStation station){
        window.dismiss();
        Refill refill=new Refill(LocalDateTime.now(),
                                 station,
                                 tracker.getGas(),
                                 null);

        //TODO
    }

    }


