package com.ceid.ui;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ceid.Network.ApiClient;
import com.ceid.Network.ApiService;
import com.ceid.Network.jsonStringParser;
import com.ceid.model.transport.Bicycle;
import com.ceid.model.transport.CityCar;
import com.ceid.model.transport.ElectricScooter;
import com.ceid.model.transport.Motorcycle;
import com.ceid.model.transport.Rental;
import com.ceid.util.Coordinates;
import com.ceid.util.GenericCallback;
import com.ceid.util.Map;
import com.ceid.util.MapWrapperReadyListener;
import com.ceid.util.PositiveInteger;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InCityVehicleScreen extends AppCompatActivity implements ActivityResultCallback<ActivityResult>, MapWrapperReadyListener, AdapterView.OnItemClickListener, GoogleMap.OnMarkerClickListener
{

    private Intent locationIntent;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private Map vehicleMap;
    private Coordinates selectedCoords = null;
    private Bundle locationScreenData = null;

    private String type;
    private int markerIcon;

    //private ArrayList<Rental> vehicleList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.in_city_vehicle_screen);

        Bundle extras = getIntent().getExtras();
        assert extras != null;

        //Initialize header text
        TextView textview = findViewById(R.id.in_city_choose_vehicle);
		textview.setText(String.format("%s %s", textview.getText(), extras.getString("type")));

        //Initialize location intent
        this.activityResultLauncher = registerForActivityResult
        (
            new ActivityResultContracts.StartActivityForResult(),
            this
        );

        //Initialize intents
        locationIntent = new Intent(this, LocationScreen.class);

        //Initialize map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
        vehicleMap = new Map(mapFragment, this, this);
        vehicleMap.setMarkerListener(this);

        //Other
        this.type = extras.getString("type");

        TextView tview = (TextView) findViewById(R.id.nearbyText);

        switch(type)
        {
            case "car":
            {
                this.markerIcon = R.drawable.in_city_car;
                tview.setText(R.string.cars_nearby);
            }
            break;

            case "motorcycle":
            {
                this.markerIcon = R.drawable.in_city_motorcycle;
                tview.setText(R.string.motorcycles_nearby);
            }
            break;

            case "bike":
            {
                this.markerIcon = R.drawable.in_city_bicycle;
                tview.setText(R.string.bicycles_nearby);
            }
            break;

            case "scooter":
            {
                this.markerIcon = R.drawable.in_city_scooter;
                tview.setText(R.string.scooters_nearby);
            }
            break;
        }
    }

    //TESTING
    @Override
    public void onMapWrapperReady()
    {
        Coordinates Patra = new Coordinates( 38.246639, 21.734573);
        vehicleMap.setZoom(12);
        vehicleMap.setPosition(Patra);
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker)
    {
        Object tag  = marker.getTag();

        if (tag != null) //Vehicle marker
        {
            // Inflate the popup window layout
            View popupView = LayoutInflater.from(this).inflate(R.layout.vehicle_popup, null);

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

            ImageView icon = popupView.findViewById(R.id.imageView);

            TextView title = popupView.findViewById(R.id.text_title);
            TextView dista = popupView.findViewById(R.id.text_dista);
            TextView rate = popupView.findViewById(R.id.text_rate);
            TextView seats = popupView.findViewById(R.id.text_seats);
            TextView plate = popupView.findViewById(R.id.text_plate);

            Rental rental = (Rental)tag;

            title.setText(String.format("%s %s (%s)", rental.getManufacturer(), rental.getModel(), rental.getManufYear()));
            dista.setText(String.format("%s: %d m", "Distance", Math.round(selectedCoords.distance(rental.getTracker().getCoords()))));
            rate.setText(String.format("%s: %f/min", "Rate", rental.getRate()));

            if (rental instanceof CityCar)
            {
                icon.setImageResource(R.drawable.in_city_car);

                seats.setText(String.format("%s: %d", "Seats", 4));
                plate.setText(String.format("%s: %s", "License Plate", ((CityCar)rental).getLicensePlate()));
            }
            else if (rental instanceof Motorcycle)
            {
                icon.setImageResource(R.drawable.in_city_motorcycle);

                seats.setVisibility(View.GONE);

                plate.setText(String.format("%s: %s", "License Plate", ((Motorcycle)rental).getLicensePlate()));
            }
            else if (rental instanceof Bicycle)
            {
                icon.setImageResource(R.drawable.in_city_bicycle);

                seats.setVisibility(View.GONE);
                plate.setVisibility(View.GONE);
            }
            else
            {
                icon.setImageResource(R.drawable.in_city_scooter);

                seats.setVisibility(View.GONE);
                plate.setVisibility(View.GONE);
            }

            Button cancel = popupView.findViewById(R.id.cancel);
            Button reserve = popupView.findViewById(R.id.reserve);

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    popupWindow.dismiss();
                }
            });

            reserve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    //Check if reservation exists
                    ApiService api = ApiClient.getApiService();
                    Call<String> call = api.checkReservation(String.valueOf(rental.getId()));

                    call.enqueue(new Callback<String>()
                    {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response)
                        {
							if (response.isSuccessful())
                            {
                                int rentalAvailable = Integer.parseInt(response.body());
                                Log.d("MYTEST", String.valueOf(rentalAvailable));

                                //Check if reservation exists
                                if (rentalAvailable == 0)
                                {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(InCityVehicleScreen.this);

                                    builder.setTitle("Reservation error");
                                    builder.setMessage("There is already a reservation for that vehicle");

                                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which)
                                        {
                                            dialog.dismiss();

                                            //Remove pin from the map
                                            marker.remove();
                                        }
                                    });

                                    builder.create().show();
                                }
                                else
                                {
                                    //Save reservation to database
                                    java.util.Map<String, Object> values = new LinkedHashMap<String, Object>();

                                    ObjectMapper mapper = new ObjectMapper();
                                    ObjectNode jsonObject = mapper.createObjectNode();

                                    jsonObject.put("selected_vehicle", rental.getId());

                                    Log.d("JSONTEST", "ID: " + String.valueOf(rental.getId()));

                                    String jsonString = "";

                                    try
                                    {
                                        jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
                                    }
                                    catch(IOException e)
                                    {
                                        e.printStackTrace();
                                    }

                                    Log.d("JSONTEST", jsonString);

                                    ApiService api=ApiClient.getApiService();

                                    Call<ResponseBody> reserveCall = api.reserveRental(jsonString);

                                    reserveCall.enqueue(new Callback<ResponseBody>() {

                                        @Override
                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
                                        {
											String data = null;
											try
											{
												data = response.body().string();
											}
											catch (IOException e)
											{
												throw new RuntimeException(e);
											}

											Log.d("JSONTEST", data);

                                            if (response.isSuccessful())
                                            {
                                                JsonNode json = jsonStringParser.parseJson(data);

                                                Log.d("JSONTEST", "Service ID: " + String.valueOf(json.get("id").asInt()));

                                                Intent intent = new Intent(v.getContext(), UnlockScreen.class);
                                                intent.putExtra("vehicle", rental);
                                                intent.putExtra("service_id", json.get("id").asInt());
                                                startActivity(intent);
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
                            }
                            else
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(InCityVehicleScreen.this);

                                builder.setTitle("Server");
                                builder.setMessage("There was an error with the server");

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
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable throwable)
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(InCityVehicleScreen.this);

                            builder.setTitle("Communication error");
                            builder.setMessage("Could not communicate with the server");

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
                    });
                }
            });
        }

        return true;

    }

    //Clicking on list item
    public void onItemClick(AdapterView<?> parent, View clickedItem, int position, long id)
    {
        //Log.d("CLICK", String.format("Position: %d", position));

        Rental rental = (Rental) clickedItem.getTag();
        vehicleMap.smoothTransition(rental.getTracker().getCoords(), vehicleMap.getZoom() < 16 ? 16:vehicleMap.getZoom());
    }

    //Clicking on the location screen
    public void onClick(View view)
    {
        if (locationScreenData != null)
            locationIntent.putExtras(locationScreenData);

        activityResultLauncher.launch(locationIntent);
    }

    //After you return from the location screen
    @Override
    public void onActivityResult(ActivityResult result)
    {
        if (result.getResultCode() == Activity.RESULT_OK)
        {
            Intent data = result.getData();
            locationScreenData = data.getExtras();

            selectedCoords = (Coordinates) locationScreenData.getSerializable("coords");

            //Display selected location
            if (selectedCoords != null)
            {
                TextInputEditText text = findViewById(R.id.location_text);
                text.setText(String.format("%s %s", getResources().getString(R.string.location),selectedCoords.toString()));

                //Set view around selected position
                vehicleMap.setZoom(locationScreenData.getFloat("zoom"));
                vehicleMap.setPosition(selectedCoords);

                //Retrieve vehicles
                this.getVehicles(new GenericCallback<ArrayList<Rental>>()
                {
                    @Override
                    public void onSuccess(ArrayList<Rental> vehicleList)
                    {
                        vehicleMap.placePin(selectedCoords, true);

                        ArrayList<Rental> validVehicles = new ArrayList<>();

                        //Place pins
                        for (Rental rental : vehicleList)
                        {
                            if (selectedCoords.withinRadius(rental.getTracker().getCoords(), 2000) && rental.isFree())
                            {
                                Marker marker = vehicleMap.placePin(rental.getTracker().getCoords(), false, markerIcon);
                                marker.setTag(rental);
                                validVehicles.add(rental);
                            }
                        }

                        ListView listView = (ListView) findViewById(R.id.listViewId);

                        listView.setAdapter(new VehicleListAdapter(InCityVehicleScreen.this,  validVehicles, InCityVehicleScreen.this.markerIcon, selectedCoords));
                        listView.setOnItemClickListener(InCityVehicleScreen.this);
                    }

                    @Override
                    public void onFailure(Exception e)
                    {

                    }
                });
            }
        }
    }

    public void getVehicles(GenericCallback<ArrayList<Rental>> callback)
    {
        ApiService api = ApiClient.getApiService();
        Call<ResponseBody> call = api.getTableData(String.format("rental_%ss", type));

        call.enqueue(new Callback<ResponseBody>()
        {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response)
            {
                if (response.isSuccessful())
                {
                    ArrayList<Rental> vehicleList = new ArrayList<>();

                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode vehicles;

					try
					{
                        String body = response.body().string();
                        Log.d("MYTEST", body);
                        vehicles = mapper.readTree(body);
					}
					catch (IOException e)
					{
						throw new RuntimeException(e);
					}

                    //Log.d("TYPE", InCityVehicleScreen.this.type);

                    for (JsonNode vehicle : vehicles)
                    {
                        if (Objects.equals(InCityVehicleScreen.this.type, "car"))
                        {
                            //Log.d("TYPE", "car");
                            vehicleList.add(new CityCar(
                                    vehicle.get("license_plate").asText(),
                                    true,
                                    vehicle.get("id").asInt(),
                                    vehicle.get("model").asText(),
                                    vehicle.get("manufacturer").asText(),
                                    vehicle.get("manuf_year").asText(),
                                    vehicle.get("rate").asDouble(),
                                    new Coordinates(
                                            vehicle.get("coords").get("x").asDouble(),
                                            vehicle.get("coords").get("y").asDouble()
                                    ),
                                    new PositiveInteger(vehicle.get("gas_level").asInt())
                            ));
                        }
                        else if (Objects.equals(InCityVehicleScreen.this.type, "motorcycle"))
                        {
                            //Log.d("TYPE", "motor");
                            vehicleList.add(new Motorcycle(
                                    vehicle.get("license_plate").asText(),
                                    true,
                                    vehicle.get("id").asInt(),
                                    vehicle.get("model").asText(),
                                    vehicle.get("manufacturer").asText(),
                                    vehicle.get("manuf_year").asText(),
                                    vehicle.get("rate").asDouble(),
                                    new Coordinates(
                                            vehicle.get("coords").get("x").asDouble(),
                                            vehicle.get("coords").get("y").asDouble()
                                    ),
                                    new PositiveInteger(vehicle.get("gas_level").asInt())
                            ));
                        }
                        else if (Objects.equals(InCityVehicleScreen.this.type, "bike"))
                        {
                            //Log.d("TYPE", "bike");
                            vehicleList.add(new Bicycle(
                                    true,
                                    vehicle.get("id").asInt(),
                                    vehicle.get("model").asText(),
                                    vehicle.get("manufacturer").asText(),
                                    vehicle.get("manuf_year").asText(),
                                    vehicle.get("rate").asDouble(),
                                    new Coordinates(
                                            vehicle.get("coords").get("x").asDouble(),
                                            vehicle.get("coords").get("y").asDouble()
                                    )
                            ));
                        }
                        else
                        {
                            //Log.d("TYPE", "scooter");
                            vehicleList.add(new ElectricScooter(
                                    true,
                                    vehicle.get("id").asInt(),
                                    vehicle.get("model").asText(),
                                    vehicle.get("manufacturer").asText(),
                                    vehicle.get("manuf_year").asText(),
                                    vehicle.get("rate").asDouble(),
                                    new Coordinates(
                                            vehicle.get("coords").get("x").asDouble(),
                                            vehicle.get("coords").get("y").asDouble()
                                    )
                            ));
                        }
                    }

                    callback.onSuccess(vehicleList);
				}
                else
                {
                    callback.onFailure(new Exception());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t)
            {
                callback.onFailure(new Exception(t));
            }
        });
    }
}
