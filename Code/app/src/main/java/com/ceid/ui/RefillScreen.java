package com.ceid.ui;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.ceid.Network.ApiClient;
import com.ceid.Network.ApiService;
import com.ceid.Network.PostHelper;
import com.ceid.model.service.GasStation;
import com.ceid.util.Coordinates;
import com.ceid.util.GenericCallback;
import com.ceid.util.Map;
import com.ceid.util.MapWrapperReadyListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;

public class RefillScreen extends AppCompatActivity implements MapWrapperReadyListener {

    private Map map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.refill);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.locationMapViewRefill);

        map = new Map(mapFragment, this,this);

        ArrayList<GasStation> gasStationList=null;
        ApiService api= ApiClient.getApiService();
        PostHelper.getGasStations(api, "gas_station", new GenericCallback<ArrayList<GasStation>>() {
            @Override
            public void onSuccess(ArrayList<GasStation> data) {

                for(GasStation station: data){
                    Marker marker = map.placePin(station.getCoords(), false);
                    marker.setTag(station);
                }

            }

            @Override
            public void onFailure(Exception e) {
                Log.d("test","fail");
            }
        });

        
    }


    @Override
    public void onMapWrapperReady() {
        Coordinates Patra = new Coordinates( 38.246639, 21.734573);
        this.map.setZoom(12);
        this.map.setPosition(Patra);
    }
}
