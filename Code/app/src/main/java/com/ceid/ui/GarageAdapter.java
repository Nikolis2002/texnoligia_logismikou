package com.ceid.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GarageAdapter extends RecyclerView.Adapter<GarageAdapter.GarageViewHolder> {
    private ArrayList<String> garageList;
    private OnGarageItemListener listener;

    public GarageAdapter(ArrayList<String> garageList,OnGarageItemListener listener){
        this.garageList=garageList;
        this.listener=listener;
    }

    @NonNull
    @Override
    public GarageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup,int viewType){
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.garage,viewGroup,false);
        return new GarageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GarageViewHolder holder,int position){
        holder.textView.setText(garageList.get(position));
    }


    @Override
    public int getItemCount(){
        return garageList.size();
    }







    public class GarageViewHolder extends RecyclerView.ViewHolder{
        public TextView textView;

        public GarageViewHolder(@NonNull View view){
            super(view);
            textView=view.findViewById(R.id.itemTextViewGarage);

            view.setOnClickListener(item->{
                int pos=getAdapterPosition();

                if(pos!=RecyclerView.NO_POSITION && listener!=null){
                    listener.onItemClick(pos);
                }
            });
        }
    }

    public interface OnGarageItemListener{
        void onItemClick(int position);
    }
}
