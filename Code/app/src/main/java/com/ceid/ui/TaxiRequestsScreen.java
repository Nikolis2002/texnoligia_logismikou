package com.ceid.ui;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ceid.Network.ApiClient;
import com.ceid.Network.ApiService;
import com.ceid.Network.jsonStringParser;
import com.ceid.model.service.TaxiRequest;
import com.ceid.model.users.TaxiDriver;
import com.ceid.util.Coordinates;
import com.ceid.util.Location;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

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
        taxiDriver = (TaxiDriver)((App) getApplicationContext()).getUser();
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
                        requestView.setLayoutManager(new LinearLayoutManager(TaxiRequestsScreen.this));
                        requestAdapter = new TaxiRequestAdapter(requestList,TaxiRequestsScreen.this,taxiDriver);
                        requestView.setAdapter(requestAdapter);

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

    public void taxiRequestSelect(View view){
        requestAdapter.clearData();
        taxiRequestSelect();
    }


}
