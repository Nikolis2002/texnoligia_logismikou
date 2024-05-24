package com.ceid.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ceid.Network.ApiClient;
import com.ceid.Network.ApiService;
import com.ceid.model.transport.Garage;
import com.ceid.model.transport.OutCityCar;
import com.ceid.model.transport.OutCityTransport;
import com.ceid.model.transport.Rental;
import com.ceid.model.transport.Van;
import com.ceid.util.Coordinates;
import com.ceid.util.GenericCallback;
import com.ceid.util.Location;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OutCityScreen extends AppCompatActivity implements AdapterView.OnItemClickListener, ActivityResultCallback<ActivityResult> {

    private RecyclerView recyclerView;
    private GarageListAdapterOld adapter;
    private Bundle locationScreenData = null;
    private Intent locationIntent;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.out_city_screen);

        //Initialize activity result launcher
        this.activityResultLauncher = registerForActivityResult
        (
                new ActivityResultContracts.StartActivityForResult(),
                this
        );

        //Initialize location intent
        locationIntent = new Intent(this, LocationScreen.class);
    }

    public void onClick(View view)
    {
        if (locationScreenData != null)
            locationIntent.putExtras(locationScreenData);

        activityResultLauncher.launch(locationIntent);
    }

    private void retrieveGarages(GenericCallback<ArrayList<Garage>> callback)
    {
        ApiService api = ApiClient.getApiService();

        Call<ResponseBody> call = api.getGarages();

        call.enqueue(new Callback<ResponseBody> (){

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
            {
                if (response.isSuccessful())
                {
                    ArrayList<Garage> garageList = new ArrayList<>();
                    String jsonString = null;

                    try
                    {
                        jsonString = response.body().string();
                    }
                    catch (IOException e)
                    {
                        throw new RuntimeException(e);
                    }

                    ArrayNode garageListData;

                    ObjectMapper mapper = new ObjectMapper();
                    try
                    {
                        garageListData = (ArrayNode)mapper.readTree(jsonString);
                    }
                    catch (JsonProcessingException e)
                    {
                        throw new RuntimeException(e);
                    }

                    for (JsonNode garageData : garageListData)
                    {
                        ArrayList<OutCityTransport> vehicleList = new ArrayList<>();
                        ArrayNode garageVehicleDataNode = (ArrayNode)garageData.get("vehicles");

						if (garageVehicleDataNode != null)
                        {
                            for (JsonNode vehicleData : garageVehicleDataNode)
                            {
                                if (Objects.equals(vehicleData.get("type").asText(), "car"))
                                {
                                    vehicleList.add(new OutCityCar(
                                        vehicleData.get("license_plate").asText(),
                                        vehicleData.get("rate").asDouble(),
                                        vehicleData.get("seats").asInt(),
                                        vehicleData.get("id").asInt(),
                                        vehicleData.get("model").asText(),
                                        vehicleData.get("manufacturer").asText(),
                                        vehicleData.get("manuf_year").asText()
                                    ));
                                }
                                else
                                {
                                    vehicleList.add(new Van(
                                        vehicleData.get("license_plate").asText(),
                                        vehicleData.get("rate").asDouble(),
                                        vehicleData.get("seats").asInt(),
                                        vehicleData.get("id").asInt(),
                                        vehicleData.get("model").asText(),
                                        vehicleData.get("manufacturer").asText(),
                                        vehicleData.get("manuf_year").asText()
                                    ));
                                }
                            }
                        }

                        garageList.add(new Garage(
                            garageData.get("id").asInt(),
                            garageData.get("name").asText(),
                            new Location(
                                garageData.get("coords").get("x").asDouble(),
                                garageData.get("coords").get("y").asDouble(),
                                garageData.get("address").asText()
                            ),
                            garageData.get("available_hours").asText(),
                            vehicleList
                        ));
                    }

                    callback.onSuccess(garageList);
                }
                else
                {

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable)
            {

            }
        });
    }

    @Override
    public void onActivityResult(ActivityResult result)
    {
        if (result.getResultCode() == Activity.RESULT_OK)
        {
            Intent data = result.getData();
            locationScreenData = data.getExtras();

            Coordinates selectedCoords = (Coordinates) locationScreenData.getSerializable("coords");

            //Display selected location
            if (selectedCoords != null)
            {
                TextInputEditText text = findViewById(R.id.location_text);
                text.setText(String.format("%s %s", getResources().getString(R.string.location),selectedCoords.toString()));

                //Get garages
                retrieveGarages(new GenericCallback<ArrayList<Garage>> ()
                {

                    @Override
                    public void onSuccess(ArrayList<Garage> garageList)
                    {
                        if (garageList.isEmpty())
                        {
                            //alternate flow
                        }
                        else
                        {
                            //Add garages to list
                            ListView listView = (ListView) findViewById(R.id.listViewId);

                            listView.setAdapter(new GarageListAdapter(OutCityScreen.this,  garageList));
                            listView.setOnItemClickListener(OutCityScreen.this);
                        }
                    }

                    @Override
                    public void onFailure(Exception e)
                    {

                    }
                });
            }
        }
    }

    //Clicking on list item
    public void onItemClick(AdapterView<?> parent, View clickedItem, int position, long id)
    {
        Garage garage = (Garage)clickedItem.getTag();

        String test=garage.getName();
        Intent intent=new Intent(OutCityScreen.this, GarageInfoScreen.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("garage", garage);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
