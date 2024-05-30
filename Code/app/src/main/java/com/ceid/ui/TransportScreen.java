package com.ceid.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.ceid.Network.ApiClient;
import com.ceid.Network.ApiService;
import com.ceid.Network.PostHelper;
import com.ceid.model.service.GasStation;
import com.ceid.model.service.Refill;
import com.ceid.model.service.RentalService;
import com.ceid.model.transport.CityCar;
import com.ceid.model.transport.ElectricScooter;
import com.ceid.model.transport.Motorcycle;
import com.ceid.model.transport.Rental;
import com.ceid.model.transport.SpecializedTracker;
import com.ceid.model.transport.VehicleTracker;
import com.ceid.model.users.Customer;
import com.ceid.model.users.User;
import com.ceid.util.Coordinates;
import com.ceid.util.GenericCallback;
import com.ceid.util.Map;
import com.ceid.util.MapWrapperReadyListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.material.textfield.TextInputEditText;

import java.time.LocalDateTime;
import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
public class TransportScreen extends AppCompatActivity implements MapWrapperReadyListener, GoogleMap.OnMapClickListener{

    private Map stationMap;
    private Chronometer timer;
    private double addNumber=20;
    private Marker carMarker=null;

    private Refill refill;

    private int[] disasterCounter= new int[2];

    private GasStation nearestGasStation = null;
    private ArrayList<GasStation> gasStationList=null;

    private Handler handler;
    private Runnable runnable;
    private int currentTimerValue = -1;

    private Customer customer;

    int id;
    Intent intent;
    RentalService service;

    Rental car;

    String trackerType;

    private boolean hasRefilled = false;

    private final ApiService api= ApiClient.getApiService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transport_screen);

        //Prevent back press
        //======================================================================================
        OnBackPressedDispatcher dispatcher = getOnBackPressedDispatcher();
        dispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                //do nothing stay on the same screen
            }
        });

        //Get data from previous screen
        //======================================================================================
        intent=getIntent();
        service = (RentalService) intent.getSerializableExtra("service");
        car= (Rental) service.getTransport();

        disasterCounter[0]=disasterCounter[1]=0;

        //Create map
        //======================================================================================
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.locationMapViewRefill);
        stationMap = new Map(mapFragment, this,this);
        stationMap.setClickable(true);
        stationMap.setClickListener(this);

        //Start movement timer
        //======================================================================================
        timer = findViewById(R.id.timer2);
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();

        //Fill balance field
        //======================================================================================
        customer  = (Customer) User.getCurrentUser();
        TextView textAvailable=findViewById(R.id.textAvailable);
        textAvailable.setText(customer.getWallet().getBalance() + "€");


        //Initially, the refill button if disabled
        enableRefillButton(false);
    }

    //Enables or disables refill options based on the selected vehicle type
    //======================================================================================
    public void enableRefill(boolean status)
    {
        if(status)
        {
            trackerType="specialized"; //Tracker type that also tracks gas

            if (gasStationList == null)
            {
                //Get gas stations
                //======================================================================================
                PostHelper.getGasStations(api, "gas_station", new GenericCallback<ArrayList<GasStation>>() {
                    @Override
                    public void onSuccess(ArrayList<GasStation> data) {

                        gasStationList = data;

                        //For every gas station, place pin
                        //======================================================================================
                        for (GasStation station : data) {
                            Marker marker = stationMap.placePin(station.getCoords(), false, R.drawable.gas_station);
                            marker.setTag(station);
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d("fail", "fail");
                    }

                });
            }
        }
        else
        {
            trackerType="vechicleTracker"; //Normal tracker that only tracks location and not gas

            //Disable all UI elements that are for refilling
            //======================================================================================
            TextView textView=findViewById(R.id.textView23);
            textView.setText("Your Location:");

            ConstraintLayout layout = findViewById(R.id.constraintLayout);
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(layout);
            constraintSet.setHorizontalBias(R.id.button5, 0.5f);
            constraintSet.applyTo(layout);

            View view=findViewById(R.id.button7);
            view.setVisibility(View.GONE);
        }
    }

    public void enableRefillButton(boolean status)
    {
        if(!hasRefilled) {
            ((Button) findViewById(R.id.button7)).setEnabled(status);
            ((Button) findViewById(R.id.button7)).setClickable(status);
        }
    }

    //Once the map is created
    //======================================================================================
    @Override
    public void onMapWrapperReady() {

        //Focus map
        //======================================================================================
        Coordinates Patra = new Coordinates( 38.246639, 21.734573);
        this.stationMap.setZoom(12);
        this.stationMap.setPosition(Patra);

        //Consume click event
        //======================================================================================
        this.stationMap.setMarkerListener(new GoogleMap.OnMarkerClickListener (){

            @Override
            public boolean onMarkerClick(@NonNull Marker marker)
            {
                return true;
            }
        });

        //Vehicle icon
        //======================================================================================
        if(car instanceof CityCar){
            id=R.drawable.in_city_car;
        }
        else if(car instanceof Motorcycle){
            id=R.drawable.in_city_motorcycle;
        } else if (car instanceof ElectricScooter) {
            id=R.drawable.in_city_scooter;
        }
        else{
            id=R.drawable.in_city_bicycle;
        }

        carMarker = stationMap.placePin(car.getTracker().getCoords(), false, id);
        carMarker.setTag(car);

        //Enable refill option only if the vehicle accepts gas
        enableRefill(car.acceptsGas());
    }

    //Clicking on the map changes vehicle location
    //On click, we communicate with the tracker on the vehicle to receive data about the vehicle's current location
    //Normally, this would be done periodically, but here it's just a simulation
    //======================================================================================
    @Override
    public void onMapClick(@NonNull LatLng latLng)
    {
        if(carMarker!=null)
        {
            PostHelper.getTrackerOfRental(api,trackerType, String.valueOf(addNumber),new GenericCallback<VehicleTracker>() {

                //Coordinates were returned successfully
                //======================================================================================
                @Override
                public void onSuccess(VehicleTracker data) {

                    TextInputEditText dista=findViewById(R.id.distaRefill);
                    addNumber=data.getDistanceTraveled(); //dummy value from the server. Distance always increments the same way
                    dista.setText("Distance Travelled: "+ String.valueOf(addNumber)+"(km)");

                    if(car.acceptsGas())
                    {
                        //Find nearest gas station from the retrieved gas stations
                        //======================================================================================
                        nearestGasStation = findNearestGasStation(new Coordinates(latLng), gasStationList);
                        Log.d("GASTEST", nearestGasStation != null ? nearestGasStation.toString() : "null");
                        enableRefillButton(nearestGasStation != null);
                    }
                }

                //Failed to get coordinates
                //======================================================================================
                @Override
                public void onFailure(Exception e) {
                    if(disasterCounter[0]==0) {
                        showAlert("Cant retrieve vehicle location");
                    }

                    disasterCounter[0] = (disasterCounter[0] + 1)%100;

                    enableRefillButton(false);
                }
            });
        }

        carMarker.remove();
        Coordinates coords=new Coordinates(latLng);
        car.getTracker().setCoords(coords);
        carMarker= stationMap.placePin(coords,false,id);


    }

    public void onRefillStart(View view)
    {
        //If, for some reason, the button was pressed while there is no nearest gas station
        if (nearestGasStation == null)
        {
            enableRefillButton(false);
            return;
        }

        //Get initial gas level
        //======================================================================================
        PostHelper.getTrackerOfRental(api,trackerType, String.valueOf(0),new GenericCallback<VehicleTracker>() {

            @Override
            public void onSuccess(VehicleTracker data)
            {
                SpecializedTracker initTracker= (SpecializedTracker) data;

                // Create the popup window
                //===========================================================================
                View popupView = LayoutInflater.from(TransportScreen.this).inflate(R.layout.refill_popup, null);

                PopupWindow refillPopup = new PopupWindow(
                        popupView,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                );

                // Show the popup window
                refillPopup.showAtLocation(
                        findViewById(android.R.id.content),
                        Gravity.CENTER,
                        0,
                        0
                );

                //Animated text on the popup window
                //===========================================================================
                handler = new Handler();
                TextView animatedTextView = popupView.findViewById(R.id.refillingText);

                runnable = new Runnable()
                {
                    @Override
                    public void run()
                    {
                        currentTimerValue = (currentTimerValue + 1)%3;

                        String str = "Refilling";

                        for (int i = 0; i < (currentTimerValue + 1); i++)
                            str = str.concat(".");

                        animatedTextView.setText(str);

                        // Schedule the next execution
                        handler.postDelayed(this, 400);
                    }
                };

                handler.post(runnable);

                //Click handlers
                //===========================================================================
                Button cancel = popupView.findViewById(R.id.cancel2);
                Button complete = popupView.findViewById(R.id.complete);

                complete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        refillPopup.dismiss();
                        handler.removeCallbacks(runnable);
                        currentTimerValue = -1;
                        onRefillEnd(refillPopup,initTracker,nearestGasStation);
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        refillPopup.dismiss();
                        currentTimerValue = -1;
                        handler.removeCallbacks(runnable);
                    }
                });

                //===========================================================================
            }

            @Override
            public void onFailure(Exception e) {
                showAlert("Failed to get initial gas level");
            }
        });
    }

    public GasStation findNearestGasStation(Coordinates coords, ArrayList<GasStation> list){
        GasStation minGasStation=list.get(0);

        for(GasStation gasStation:list){
            if(coords.distance(gasStation.getCoords())<coords.distance(minGasStation.getCoords())){
                minGasStation=gasStation;
            }
        }
        Log.d("test", String.valueOf(coords.distance(minGasStation.getCoords())));
        if (coords.distance(minGasStation.getCoords()) < 200)
        {
            return minGasStation;
        }
        else return null;
    }

    public void showAlert(String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(TransportScreen.this);

        builder.setTitle("Connection error");
        builder.setMessage(msg);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();

            }
        });

        AlertDialog alert = builder.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }

    public void onRefillEnd(PopupWindow window, SpecializedTracker initTracker, GasStation station){

        //Get final gas level
        //======================================================================================
        PostHelper.getTrackerOfRental(api,trackerType, String.valueOf(0),new GenericCallback<VehicleTracker>() {
            @Override
            public void onSuccess(VehicleTracker data) {

                SpecializedTracker  finalTracker= (SpecializedTracker) data;

                //Data from trackers is random
                //For the sake of this demonstration, let's assume the second tracker has more gas (after refill)
                //To simulate this, we just take absolute difference of two gas levels
                int diff=finalTracker.getGas().posDiff(initTracker.getGas());


                //Calculate gas price and return money to customer wallet
                //======================================================================================

                double gasPrice = station.calculateGasPrice(diff);

                customer.getWallet().addToWallet(gasPrice);
                PostHelper.addToWallet(api, customer.getUsername(), gasPrice);

                TextView textAvailable=findViewById(R.id.textAvailable);
                textAvailable.setText(customer.getWallet().getBalance() + "€");

                //Create the refill object
                //======================================================================================
                refill = new Refill(LocalDateTime.now(),
                station,
                initTracker.getGas(),
                finalTracker.getGas(),
                true);

                service.setRefill(refill);

                //Gas threshold
                //======================================================================================
                int threshold=5;

                if(diff>threshold){
                    refill.calculatePoints(service);
                }
                else
                {
                    //belowThresholdMsg
                    Toast.makeText(getApplicationContext(), "Refill amount too small. You gained 0 points.", Toast.LENGTH_SHORT).show();
                }

                hasRefilled = true;
                ((Button) findViewById(R.id.button7)).setEnabled(false);
                ((Button) findViewById(R.id.button7)).setClickable(false);
            }



            //Failed to get final gas level
            //======================================================================================
            @Override
            public void onFailure(Exception e) {

                //We give the user 100 retries before we force him to STOP RETRYING, IT AIN'T GONNA WORK THE 101st TIME, JUST LET IT GO BRUH, IT'S OVER
                if(disasterCounter[1]<100) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(TransportScreen.this);

                    builder.setTitle("Error");
                    builder.setMessage("Failed to get final gas level");
                    builder.setCancelable(false);

                    builder.setPositiveButton("Try again", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            onRefillEnd(window,initTracker,station);
                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            failedRefill(window,initTracker,station);
                        }
                    });

                    AlertDialog finalTrackerFailure = builder.create();
                    finalTrackerFailure.setCanceledOnTouchOutside(false);
                    finalTrackerFailure.show();
                }
                if(disasterCounter[1]==100) {
                    Toast.makeText(getApplicationContext(), "You reached the max amount of re-tries!", Toast.LENGTH_LONG).show();

                    failedRefill(window,initTracker,station);
                    disasterCounter[1] = 0;
                }
                disasterCounter[1]++;
            }
        });
    }

    public void failedRefill(PopupWindow window, SpecializedTracker tracker, GasStation station){

        //Refill failed. Create Refill object marking the refill as incomplete, as we don't know final gas level
        refill=new Refill(LocalDateTime.now(),
                                 station,
                                 tracker.getGas(),
                                 null);

        hasRefilled =true;
        ((Button) findViewById(R.id.button7)).setEnabled(false);
        ((Button) findViewById(R.id.button7)).setClickable(false);
    }

    public void endRoute(View view)
    {
        //Get tracker data before ending the ride
        //========================================================================================

        PostHelper.getTrackerOfRental(api,trackerType, String.valueOf(addNumber),new GenericCallback<VehicleTracker>() {

            //Data was returned successfully
            //======================================================================================
            @Override
            public void onSuccess(VehicleTracker tracker)
            {
                ((Rental)service.getTransport()).setTracker(tracker);

                if (tracker.isStopped())
                {
                    //Movement data
                    //========================================================================================
                    Intent intent=new Intent(getApplicationContext(), EndRideScreen.class);
                    Bundle bundle=new Bundle();

                    timer.stop();

                    long elapsedMillis = SystemClock.elapsedRealtime() - timer.getBase();

                    Log.d("TIMETEST", String.format("%f", ((double)elapsedMillis/60000)));

                    double time = ((double)elapsedMillis/60000);

                    bundle.putSerializable("service",service);
                    bundle.putString("timestring", (String) timer.getText());

                    Log.d("TIMETEST", (String)timer.getText());

                    //Calculate cost
                    //========================================================================================
                    double routeCost = ((Rental) service.getTransport()).calculateCharge(time);
                    double balance = customer.getWallet().getBalance();

                    bundle.putDouble("cost", routeCost);

                    if (balance < routeCost)
                        insufficientBalance();
                    else
                        Toast.makeText(getApplicationContext(), "Payment executed successfully", Toast.LENGTH_SHORT).show();

                    customer.getWallet().withdraw(routeCost);
                    PostHelper.withdraw(api, customer.getUsername(), routeCost);

                    //Display EndRouteScreen
                    //=========================================================================================
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }
                else //Vehicle is not stopped
                {
                    Toast.makeText(getApplicationContext(), "Vehicle must be stopped before ending ride", Toast.LENGTH_SHORT).show();
                }
            }

            //Tracker error
            //======================================================================================
            @Override
            public void onFailure(Exception e)
            {
                showAlert("Failed to end ride due to tracker communication failure.");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (handler != null && runnable!=null)
            handler.removeCallbacks(runnable);
    }

    //ERRORS
    //===================================================================================================
    public void insufficientBalance()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Insufficient balance");
        builder.setMessage("Your wallet balance was not enough for the payment. The payment will still be executed, but you will be left with negative balance!");
        builder.setCancelable(false);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }
}




