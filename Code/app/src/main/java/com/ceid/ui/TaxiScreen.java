package com.ceid.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ceid.Network.ApiClient;
import com.ceid.Network.ApiService;
import com.ceid.Network.jsonStringParser;
import com.ceid.model.payment_methods.Payment;
import com.ceid.model.service.TaxiService;
import com.ceid.model.users.Customer;
import com.ceid.model.users.User;
import com.ceid.util.Coordinates;
import com.ceid.util.DateFormat;
import com.ceid.util.Location;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaxiScreen extends AppCompatActivity implements ActivityResultCallback<ActivityResult>{
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private Bundle destinationScreenData;
    private Location location;
    private Coordinates destinationCoord;
    private float zoom;
    Customer customer;
    double finalCostEstimated;
    ApiService api= ApiClient.getApiService();
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taxi_screen);
        enableTaxiBtn(false);
        gpsLocation();

        customer=(Customer) User.getCurrentUser();

        TextView textView = findViewById(R.id.balance);
        String balance= customer.getWallet().getBalance() + "€";
        textView.setText(balance);

        this.activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),this
        );
    }



    public void findTaxi(View view){
        boolean fieldCheck=checkLocField();

        if(!fieldCheck){
            Toast.makeText(getApplicationContext(), "Set a destination!", Toast.LENGTH_SHORT).show();
        }

        if(paymentCheck()==-1){
            Toast.makeText(getApplicationContext(), "Select a payment method!", Toast.LENGTH_SHORT).show();
        }

        if(fieldCheck && paymentCheck()!=-1){

            RadioGroup paymentRadioGroup = findViewById(R.id.paymentRadioGroup);
            int selectedPayment = paymentRadioGroup.getCheckedRadioButtonId();
            RadioButton paymentRadioButton = findViewById(selectedPayment);
            String payment = paymentRadioButton.getText().toString().toUpperCase();


            if(payment.equals("CASH")){

                List<Map<String,Object>> values = new ArrayList<>();
                Map<String, Object> taxiReservation = new LinkedHashMap<>();
                taxiReservation.put("payment_customer_username",customer.getUsername());
                taxiReservation.put("payment_method",payment);
                taxiReservation.put("service_creation_date", DateFormat.format(LocalDateTime.now()));
                taxiReservation.put("taxiReq_pickup_location",location.coordsToJson());
                taxiReservation.put("taxiReq_destination",destinationCoord.coordsToJson());
                values.add(taxiReservation);

                String jsonString = jsonStringParser.createJsonString(values);

                Call<String> call = api.insertTaxiService(jsonString);

                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {

                        if(response.isSuccessful()){
                            String service=response.body();
                            int serviceId= Integer.parseInt(service);
                            TaxiService taxiService = new TaxiService(
                                    serviceId,
                                    new Payment(
                                            0,
                                            Payment.Method.CASH
                                    )
                            );
                            Intent intent = new Intent(TaxiScreen.this, TaxiWaitScreen.class);
                            intent.putExtra("taxiService",taxiService);
                            startActivity(intent);
                            finish();
                        }else{
                            System.out.println("Error message");
                        }

                    }
                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable throwable) {
                        System.out.println("Error message");
                    }
                });
            }else{
                double balance=customer.getWallet().getBalance();

                if(balance<finalCostEstimated){
                    Toast.makeText(getApplicationContext(), "Υou don't have the estimated amount in your wallet ", Toast.LENGTH_SHORT).show();
                }else{
                    List<Map<String,Object>> values = new ArrayList<>();
                    Map<String, Object> taxiReservation = new LinkedHashMap<>();
                    taxiReservation.put("payment_customer_username",customer.getUsername());
                    taxiReservation.put("payment_method",payment);
                    taxiReservation.put("service_creation_date", DateFormat.format(LocalDateTime.now()));
                    taxiReservation.put("taxiReq_pickup_location",location.coordsToJson());
                    taxiReservation.put("taxiReq_destination",destinationCoord.coordsToJson());
                    values.add(taxiReservation);

                    String jsonString = jsonStringParser.createJsonString(values);

                    Call<String> call = api.insertTaxiService(jsonString);

                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {

                            if(response.isSuccessful()){
                                String service=response.body();
                                int serviceId = Integer.parseInt(service);
                                TaxiService taxiService = new TaxiService(
                                        serviceId,
                                        new Payment(
                                                0,
                                                Payment.Method.WALLET
                                        )
                                );
                                Intent intent = new Intent(TaxiScreen.this, TaxiWaitScreen.class);
                                intent.putExtra("taxiService",taxiService);
                                startActivity(intent);
                                finish();
                            }else{
                                System.out.println("Error message");
                            }

                        }
                        @Override
                        public void onFailure(@NonNull Call<String> call, @NonNull Throwable throwable) {
                            System.out.println("Error message");
                        }
                    });
                }

            }


        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    public void insertDestination(View view){
        Intent destinationIntent = new Intent(TaxiScreen.this, LocationScreen.class);
        destinationScreenData = new Bundle();
        destinationScreenData.putSerializable("coords",destinationCoord);
        destinationScreenData.putString("text","Choose your Destination");
        destinationScreenData.putFloat("zoom",zoom);
        destinationScreenData.putSerializable("location",location);
        destinationIntent.putExtras(destinationScreenData);
        activityResultLauncher.launch(destinationIntent);
    }

    private int paymentCheck(){
        RadioGroup radioGroup = findViewById(R.id.paymentRadioGroup);

        return radioGroup.getCheckedRadioButtonId();
    }

    private void enableTaxiBtn(Boolean action){
        Button button = findViewById(R.id.findCabButton);
        button.setEnabled(action);
    }

    private boolean checkLocField(){
        TextInputEditText destCoords = findViewById(R.id.endPointInput);
        String end = destCoords.getText().toString();

        return !(end.isEmpty());
    }

    private void gpsLocation(){

        List<Location> locationList= new ArrayList<>();

        locationList.add(new Location(
                38.24581944113243,
                21.735667393600977,
                "Πλ. Βασιλέως Γεωργίου Α"
        ));

        locationList.add(new Location(
                38.26156326500886,
                21.744230924244444,
                "Στάδιο Παναχαϊκής Κώστας Δαβουρλής"
        ));

        locationList.add(new Location(
                38.23805202046475,
                21.72593019402509,
                "Veso Mare"
        ));

        locationList.add(new Location(
                38.28642678834642,
                21.78648559358893,
                "Πανεπιστημιούπολη Πατρών"
        ));

        locationList.add(new Location(
                38.221008554956555,
                21.741725145302286,
                "Ακρωτηρίου 128-132"
        ));

        locationList.add(new Location(
                38.23119708759673,
                21.763954185920586,
                "Ηρακλέους 13"
        ));

        Random random = new Random();
        int randomLocation = random.nextInt(locationList.size());
        location = locationList.get(randomLocation);
        TextInputEditText fromInput = findViewById(R.id.startPointInput);
        fromInput.setText(location.getAddress());
    }


    @Override
    public void onActivityResult(ActivityResult result) {
        if (result.getResultCode() == Activity.RESULT_OK){
            Intent data = result.getData();
            assert data != null;
            destinationScreenData = data.getExtras();

            assert destinationScreenData != null;
            destinationCoord = (Coordinates) destinationScreenData.getSerializable("coords");
            zoom = destinationScreenData.getFloat("zoom");

            if (destinationCoord != null){
                TextInputEditText destCoords = findViewById(R.id.endPointInput);
                destCoords.setText(destinationCoord.toString());
                double taxiCost=location.estimateTaxiCost(destinationCoord);
                TextView estimateCost = findViewById(R.id.estimatedCost);
                String taxiCostFormatted= new DecimalFormat ("#.00").format(taxiCost);
                String taxiCostFormat = taxiCostFormatted + "€";
                estimateCost.setText(taxiCostFormat);
                TextView finalCostText = findViewById(R.id.finalCost);
                finalCostEstimated = taxiCost + 1.5;
                String finaCostEstimatedString = new DecimalFormat ("#.00").format(finalCostEstimated);
                String finalCost = finaCostEstimatedString + "€";
                finalCostText.setText(finalCost);
            }


            boolean fieldCheck=checkLocField();
            enableTaxiBtn(fieldCheck);

            if(!fieldCheck){
                Toast.makeText(getApplicationContext(), "Set a destination", Toast.LENGTH_SHORT).show();
            }

         }else{
            Toast.makeText(getApplicationContext(), "Set a destination", Toast.LENGTH_SHORT).show();
            boolean fieldCheck=checkLocField();
            enableTaxiBtn(fieldCheck);
        }
    }
}
