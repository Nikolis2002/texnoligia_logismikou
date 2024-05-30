package com.ceid.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ceid.model.transport.Garage;
import com.ceid.model.transport.OutCityCar;
import com.ceid.model.transport.OutCityTransport;

import java.util.ArrayList;

public class OutCityVehicleListAdapter extends BaseAdapter
{
    private Context context;
    private ArrayList<OutCityTransport> vehicles;

    public OutCityVehicleListAdapter(Context context, ArrayList<OutCityTransport> vehicles) {
        this.context = context;
        this.vehicles = vehicles;
    }

    public int getCount()
    {
        if (vehicles.isEmpty())
            return 1; //Display one empty row

        return vehicles.size();
    }

    public Object getItem(int arg0) {
        return null;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (!vehicles.isEmpty())
        {
            if (convertView == null)
            {
                convertView = LayoutInflater.from(this.context).inflate(R.layout.row, parent, false);
            }

            convertView.setTag(vehicles.get(position));

            TextView title = (TextView) convertView.findViewById(R.id.txtTitle);
            TextView subtitle = (TextView) convertView.findViewById(R.id.txtSubtitle);
            ImageView imgview = (ImageView) convertView.findViewById(R.id.imgIcon);

            title.setText(String.format("%s %s", vehicles.get(position).getManufacturer(), vehicles.get(position).getModel()));
            //subtitle.setText(String.format("%s", vehicles.get(position).getAddress()));

            if (vehicles.get(position) instanceof OutCityCar)
                imgview.setImageResource(R.drawable.out_city_car);
            else imgview.setImageResource(R.drawable.out_city_van);

            return (convertView);
        }
        else
        {
            if (convertView == null)
            {
                convertView = emptyRow(parent);
            }

            return convertView;
        }
    }

    public View emptyRow(ViewGroup parent)
    {
        return LayoutInflater.from(this.context).inflate(R.layout.out_city_vehicles_empty_row, parent, false);
    }
}