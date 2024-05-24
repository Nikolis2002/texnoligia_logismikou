package com.ceid.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ceid.Network.ApiClient;
import com.ceid.Network.ApiService;
import com.ceid.Network.PostHelper;
import com.ceid.model.service.GasStation;
import com.ceid.model.transport.CityCar;
import com.ceid.model.transport.ElectricScooter;
import com.ceid.model.transport.Motorcycle;
import com.ceid.model.transport.Rental;
import com.ceid.model.transport.SpecializedTracker;
import com.ceid.model.transport.VehicleTracker;
import com.ceid.util.Coordinates;
import com.ceid.util.GenericCallback;
import com.ceid.util.Map;
import com.ceid.util.MapWrapperReadyListener;
import com.ceid.util.PositiveInteger;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;

public class RefillScreen extends AppCompatActivity implements MapWrapperReadyListener, GoogleMap.OnMarkerDragListener {

    private Map map;
    String trackerType;
    private final ApiService api= ApiClient.getApiService();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.refill);



        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.locationMapViewRefill);

        map = new Map(mapFragment, this,this);
    }


    @Override
    public void onMapWrapperReady() {
        Coordinates Patra = new Coordinates( 38.246639, 21.734573);
        this.map.setZoom(12);
        this.map.setPosition(Patra);

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

        int id;
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


        Marker marker =map.placePin(car.getTracker().getCoords(), false,id);
        marker.setTag(car);
        map.getMap().setOnMarkerDragListener(this);

        ArrayList<GasStation> gasStationList=null;

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
                    Log.d("test", "fail");
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

        marker.setDraggable(true);
    }

    @Override
    public void onMarkerDrag(@NonNull Marker marker) {
    }

    @Override
    public void onMarkerDragEnd(@NonNull Marker marker) {
        PostHelper.getTackerOfRental(api,trackerType,new GenericCallback<VehicleTracker>() {

            @Override
            public void onSuccess(VehicleTracker data) {

            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    @Override
    public void onMarkerDragStart(@NonNull Marker marker) {

    }
}
