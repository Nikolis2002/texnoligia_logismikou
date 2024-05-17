package com.ceid.ui;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ceid.model.service.TaxiRequest;

import java.util.List;

public class TaxiRequestAdapter extends RecyclerView.Adapter<TaxiRequestAdapter.RequestView> {
    private List<TaxiRequest> taxiRequest;

    public TaxiRequestAdapter(List<TaxiRequest> taxiRequest){
        this.taxiRequest=taxiRequest;
    }

    public static class RequestView extends RecyclerView.ViewHolder {
        TextView number;
        TextView start;
        TextView end;

        public RequestView(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textView.setText(dataList.get(position));
    }

}
