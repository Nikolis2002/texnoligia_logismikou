package com.ceid.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ceid.Network.ApiClient;
import com.ceid.Network.ApiService;
import com.ceid.Network.jsonStringParser;
import com.ceid.model.service.TaxiRequest;
import com.ceid.model.users.TaxiDriver;
import com.ceid.model.users.User;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaxiRequestsScreen extends AppCompatActivity {
    private RecyclerView requestView;
    private TaxiRequestAdapter requestAdapter;
    private TaxiDriver taxiDriver;
    ApiService api= ApiClient.getApiService();

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taxi_request_screen);

        //Disable back button in this screen
        //=================================================================================
        OnBackPressedDispatcher dispatcher = getOnBackPressedDispatcher();
        dispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(TaxiRequestsScreen.this, MainScreenTaxi.class);
                startActivity(intent);
                finish();
            }
        });

        //Get taxi Driver data
        //=================================================================================
        taxiDriver = (TaxiDriver) User.getCurrentUser();

        //Retrieve Taxi Requests from database
        //=================================================================================
        taxiRequestSelect();
    }

    public void taxiRequestSelect(){

        Call<ResponseBody> call = api.getTableData("selectTaxiRequests");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        ArrayList<TaxiRequest> requestList=jsonStringParser.parseTaxiRequest(response);
                        requestView = findViewById(R.id.requestView);

                        if(!requestList.isEmpty()) {
                            //Display the Requests
                            //=================================================================================
                            requestView.setLayoutManager(new LinearLayoutManager(TaxiRequestsScreen.this));
                            requestAdapter = new TaxiRequestAdapter(requestList, TaxiRequestsScreen.this, taxiDriver);
                            requestView.setAdapter(requestAdapter);
                        }else{
                            requestView.setVisibility(View.GONE);
                            TextView textView=findViewById(R.id.emptyRequest);
                            String text="There are not active taxi requests";
                            textView.setText(text);
                            textView.setVisibility(View.VISIBLE);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {

            }
        });



    }

    //Refresh button for Taxi Requests
    //=================================================================================
    public void taxiRequestSelect(View view){
        requestAdapter.clearData();
        taxiRequestSelect();
    }




}
