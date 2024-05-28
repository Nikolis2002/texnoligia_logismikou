package com.ceid.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ceid.Network.ApiClient;
import com.ceid.Network.ApiService;
import com.ceid.Network.jsonStringParser;
import com.ceid.model.service.TaxiRequest;
import com.ceid.model.users.TaxiDriver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaxiRequestAdapter extends RecyclerView.Adapter<TaxiRequestAdapter.RequestView> {
    private List<TaxiRequest> taxiRequests;
    private Context context;
    private ApiService api= ApiClient.getApiService();
    private TaxiDriver taxiDriver;
    private TaxiRequest taxiRequest;

    public TaxiRequestAdapter(List<TaxiRequest> taxiRequest, Context context, TaxiDriver taxiDriver){
        this.taxiRequests=taxiRequest;
        this.context=context;
        this.taxiDriver=taxiDriver;
    }

    public static class RequestView extends RecyclerView.ViewHolder {
        TextView number;
        TextView start;
        TextView end;
        Button accept;

        public RequestView(View requestView) {
            super(requestView);
            number = requestView.findViewById(R.id.requestNumber);
            start = requestView.findViewById(R.id.pickUp);
            end = requestView.findViewById(R.id.destination);
            accept = requestView.findViewById(R.id.acceptButton);
        }
    }

    @NonNull
    @Override
    public RequestView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.taxi_requests_view, parent, false);
        return new RequestView(view);
    }

    @Override
    public void onBindViewHolder(RequestView holder, int position) {
        taxiRequest = taxiRequests.get(position);
        String text="Request " + (position+1);
        holder.number.setText(text);
        holder.start.setText(String.valueOf(taxiRequest.getPickupLocation()));
        holder.end.setText(String.valueOf(taxiRequest.getDestination()));

        holder.accept.setOnClickListener(v -> {
            taxiDriver.getTaxi().gpsLocation();
            if(taxiDriver.getTaxi().getCoords().getLat()==-1 && taxiDriver.getTaxi().getCoords().getLng()==-1) {
                Toast.makeText(context.getApplicationContext(),"The location could not be found", Toast.LENGTH_SHORT).show();
            }else {
                checkRequest();
            }
        });
    }

    public void checkRequest(){
        List<Map<String,Object>> values = new ArrayList<>();
        Map<String, Object> taxiReservationCheck = new LinkedHashMap<>();
        taxiReservationCheck.put("request_id_in",taxiRequest.getId());
        values.add(taxiReservationCheck);

        String jsonString = jsonStringParser.createJsonString("checkTaxiRequest",values);
        Call<ResponseBody> call = api.getFunction(jsonString);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                if(response.isSuccessful()){
                    try {
                        boolean status = jsonStringParser.getbooleanFromJson(response);

                        if(status) {
                            acceptRequest();
                        }else{
                            TaxiRequestsScreen activity = (TaxiRequestsScreen) context;
                            activity.taxiRequestSelect();
                            Toast.makeText(context.getApplicationContext(),"The request is not available", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }else{
                    System.out.println("Error message");
                }

            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {
                System.out.println("Error message");
            }
        });
    }

    public void acceptRequest(){
        List<Map<String,Object>> values = new ArrayList<>();
        Map<String, Object> taxiReservationConfirm = new LinkedHashMap<>();
        taxiReservationConfirm.put("id",taxiRequest.getId());
        taxiReservationConfirm.put("username",taxiDriver.getUsername());
        values.add(taxiReservationConfirm);

        String jsonString = jsonStringParser.createJsonString("acceptTaxiRequest",values);
        Call<ResponseBody> call = api.getFunction(jsonString);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                if(response.isSuccessful()){
                    Intent intent = new Intent(context, TaxiTransportScreen.class);
                    intent.putExtra("taxiRequest",taxiRequest);
                    context.startActivity(intent);
                    ((Activity) context).finish();
                }else{
                    System.out.println("Error message");
                }

            }
            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {
                System.out.println("Error message");
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    public void clearData() {
        taxiRequests.clear();
        notifyDataSetChanged();
    }

    public int getItemCount() {
        return taxiRequests.size();
    }


}