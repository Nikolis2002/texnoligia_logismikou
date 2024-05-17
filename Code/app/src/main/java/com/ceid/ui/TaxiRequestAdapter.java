package com.ceid.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaxiRequestAdapter extends RecyclerView.Adapter<TaxiRequestAdapter.RequestView> {
    private List<String> taxiRequest;

    public TaxiRequestAdapter(List<String> taxiRequest){
        this.taxiRequest=taxiRequest;
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

        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Button clicked for item: ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public int getItemCount() {
        return taxiRequest.size();
    }

    public int getItemCount() {
        return taxiRequest.size();
    }

}