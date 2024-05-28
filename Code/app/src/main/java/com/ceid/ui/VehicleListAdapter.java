package com.ceid.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ceid.model.transport.Rental;
import com.ceid.util.Coordinates;

import java.util.ArrayList;

public class VehicleListAdapter extends BaseAdapter
{
	private Context context;
	private ArrayList<Rental> vehicles;
	private int icon;
	private Coordinates pos;

	public VehicleListAdapter(Context context, ArrayList<Rental> vehicles, int icon, Coordinates pos) {
		this.context = context;
		this.icon = icon;
		this.vehicles = vehicles;
		this.pos = pos;
	}

	public int getCount() {
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
		if (convertView == null)
		{
			convertView = LayoutInflater.from(this.context).inflate(R.layout.row, parent, false);
		}
		convertView.setTag(vehicles.get(position));

		TextView title = (TextView) convertView.findViewById(R.id.txtTitle);
		TextView subtitle = (TextView) convertView.findViewById(R.id.txtSubtitle);
		ImageView imgview = (ImageView) convertView.findViewById(R.id.imgIcon);

		float dista = pos.distance(vehicles.get(position).getTracker().getCoords());

		title.setText(String.format("%s %s", vehicles.get(position).getManufacturer(), vehicles.get(position).getModel()));
		subtitle.setText(String.format("%s: %s m", context.getResources().getString(R.string.distance), Math.round(dista)));
		imgview.setImageResource(this.icon);

		return (convertView);
	}

	public void clearData() {
		vehicles.clear();
		notifyDataSetChanged();
	}
}