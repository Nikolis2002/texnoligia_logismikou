package com.ceid.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaxiRequestAdapter extends RecyclerView.Adapter<TaxiRequestAdapter.RequestView> {
    private List<String> taxiRequest;
    private Context context;

    public TaxiRequestAdapter(List<String> taxiRequest,Context context){
        this.taxiRequest=taxiRequest;
        this.context=context;
    }

    public static class RequestView extends RecyclerView.ViewHolder {
        TextView number;
        TextView start;
        TextView end;
        Button accept;
        Button reject;
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
        holder.number.setText("test");

        holder.accept.setOnClickListener(v -> {
            Intent intent = new Intent(context,TransportScreen.class);
            context.startActivity(intent);
        });
    }

    public int getItemCount() {
        return taxiRequest.size();
    }


}