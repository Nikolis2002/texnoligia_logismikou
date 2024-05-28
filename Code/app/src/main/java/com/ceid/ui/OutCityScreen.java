package com.ceid.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.ceid.Network.ApiClient;
import com.ceid.Network.ApiService;
import com.ceid.model.transport.Garage;
import com.ceid.model.transport.OutCityCar;
import com.ceid.model.transport.OutCityTransport;
import com.ceid.model.transport.Van;
import com.ceid.util.Coordinates;
import com.ceid.util.GenericCallback;
import com.ceid.util.Location;
import com.ceid.util.Map;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OutCityScreen extends AppCompatActivity implements AdapterView.OnItemClickListener, ActivityResultCallback<ActivityResult> {

    private RecyclerView recyclerView;

    private Bundle locationScreenData = null;
    private Intent locationIntent;

    private ActivityResultLauncher<Intent> activityResultLauncher;
    private ArrayList<Coordinates> polygon = null;

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
                        //Construct the garages
                        //===============================================================================
                        ArrayList<OutCityTransport> vehicleList = new ArrayList<>();
                        ArrayNode garageVehicleDataNode = (ArrayNode)garageData.get("vehicles");

                        //Check if the garage is within selected polygon
                        //--------------------------------------------------------------------------
                        Coordinates garageCoords = new Coordinates(
                                garageData.get("coords").get("x").asDouble(),
                                garageData.get("coords").get("y").asDouble()
                        );

                        if (!Map.withinPolygon(garageCoords, polygon))
                            continue;

                        if (garageVehicleDataNode != null)
                        {
                            //Make the vehicles inside the garage
                            //--------------------------------------------------------------------------
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

                        //Make the garage
                        //--------------------------------------------------------------------------
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
            polygon = (ArrayList<Coordinates>)locationScreenData.getSerializable("polygon");

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
                        //Add garages to list
                        ListView listView = (ListView) findViewById(R.id.listViewId);

                        listView.setAdapter(null);

                        if (garageList.isEmpty())
                        {
                            noGarageMsg();
                        }
                        else
                        {
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

    //ERRORS
    //========================================================================================

    public void noGarageMsg()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("No garages");
        builder.setMessage("There are no garages in the selected area");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                OutCityScreen.this.onClick(null);
            }
        });

        builder.create().show();
    }
}
