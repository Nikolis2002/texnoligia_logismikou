package com.ceid.ui;

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
    ApiService api= ApiClient.getApiService();
    TaxiDriver taxiDriver;

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
        TaxiRequest taxiRequest = taxiRequests.get(position);
        holder.number.setText("Request " + (position+1));
        holder.start.setText(String.valueOf(taxiRequest.getPickupLocation()));
        holder.end.setText(String.valueOf(taxiRequest.getDestination()));

        holder.accept.setOnClickListener(v -> {

            taxiDriver.getTaxi().gpsLocation();

            List<Map<String,Object>> values = new ArrayList<>();
            Map<String, Object> taxiReservationCheck = new LinkedHashMap<>();
            taxiReservationCheck.put("request_id",taxiRequest.getId());
            values.add(taxiReservationCheck);

            String jsonString = jsonStringParser.createJsonString("checkTaxiReservation",values);
            Call<ResponseBody> call = api.getFunction(jsonString);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                    if(response.isSuccessful()){
                        try {
                            boolean status = jsonStringParser.getbooleanFromJson(response);

                            if(status) {
                                List<Map<String,Object>> values = new ArrayList<>();
                                Map<String, Object> taxiReservationConfirm = new LinkedHashMap<>();
                                taxiReservationConfirm.put("id",taxiRequest.getId());
                                taxiReservationConfirm.put("username",taxiDriver.getUsername());
                                values.add(taxiReservationConfirm);

                                String jsonString = jsonStringParser.createJsonString("acceptTaxiRequest",values);
                                Call<ResponseBody> call2 = api.getFunction(jsonString);

                                call2.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(@NonNull Call<ResponseBody> call2, @NonNull Response<ResponseBody> response) {

                                        if(response.isSuccessful()){
                                            Intent intent = new Intent(context,TransportScreen.class);
                                            intent.putExtra("taxiRequest",taxiRequest);
                                            context.startActivity(intent);

                                        }else{
                                            System.out.println("Error message");
                                        }

                                    }
                                    @Override
                                    public void onFailure(@NonNull Call<ResponseBody> call2, @NonNull Throwable throwable) {
                                        System.out.println("Error message");
                                    }
                                });


                            }else{
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
        });
    }

    public void clearData() {
        taxiRequests.clear();
        notifyDataSetChanged();
    }

    public int getItemCount() {
        return taxiRequests.size();
    }


}