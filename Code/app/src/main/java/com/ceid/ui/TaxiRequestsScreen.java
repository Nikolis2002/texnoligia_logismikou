package com.ceid.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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
        taxiDriver = (TaxiDriver) User.getCurrentUser();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(TaxiRequestsScreen.this, MainScreenTaxi.class);
        startActivity(intent);
        finish();
    }

    public void taxiRequestSelect(View view){
        requestAdapter.clearData();
        taxiRequestSelect();
    }




}
