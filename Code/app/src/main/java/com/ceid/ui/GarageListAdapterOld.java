package com.ceid.ui;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ceid.model.transport.Garage;

import java.util.ArrayList;

public class GarageListAdapterOld extends RecyclerView.Adapter<GarageListAdapterOld.GarageViewHolder> {
    private ArrayList<Garage> garageList;
    private OnGarageItemListener listener;

    public GarageListAdapterOld(ArrayList<Garage> garageList, OnGarageItemListener listener){
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
        holder.textView.setText("sus");
        Log.d("GARAGE","test1");
    }

    @Override
    public int getItemCount(){
        return garageList.size();
    }

    //Classes
    //==================================================================================

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
