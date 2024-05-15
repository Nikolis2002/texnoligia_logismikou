package com.ceid.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ceid.model.transport.Garage;
import com.ceid.model.transport.Rental;
import com.ceid.util.Coordinates;

import java.util.ArrayList;

public class GarageListAdapter extends BaseAdapter
{
    private Context context;
    private ArrayList<Garage> garages;
    private int icon;

    public GarageListAdapter(Context context, ArrayList<Garage> garages) {
        this.context = context;
        this.icon = R.drawable.garage_icon;
        this.garages = garages;
    }

    public int getCount() {
        return garages.size();
    }

    public Object getItem(int arg0) {
        return null;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = LayoutInflater.from(this.context).inflate(R.layout.garage_row, parent, false);
        }
        convertView.setTag(garages.get(position));

        TextView title = (TextView) convertView.findViewById(R.id.txtTitle);
        TextView subtitle = (TextView) convertView.findViewById(R.id.txtSubtitle);
        ImageView imgview = (ImageView) convertView.findViewById(R.id.imgIcon);

        title.setText(String.format("%s", garages.get(position).getName()));
        subtitle.setText(String.format("%s", garages.get(position).getAddress()));
        imgview.setImageResource(this.icon);

        return (convertView);
    }
}