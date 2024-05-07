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

public class MyListAdapter extends BaseAdapter
{
	private Context context;
	private ArrayList<Rental> vehicles;
	private String title;
	private int icon;
	private Coordinates pos;

	public MyListAdapter(Context context, ArrayList<Rental> vehicles, String title, int icon, Coordinates pos) {
		this.context = context;
		this.title = title;
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
		LayoutInflater inflater = LayoutInflater.from(this.context);

		View row;
		row = inflater.inflate(R.layout.row, parent, false);

		TextView title = (TextView) row.findViewById(R.id.txtTitle);
		TextView subtitle = (TextView) row.findViewById(R.id.txtSubtitle);
		ImageView imgview = (ImageView) row.findViewById(R.id.imgIcon);

		float dista = pos.distance(vehicles.get(position).getTracker().getCoords());

		title.setText(this.title);
		subtitle.setText(String.format("%s: %s", context.getResources().getString(R.string.distance), dista));
		imgview.setImageResource(this.icon);

		return (row);
	}
}