package com.ceid.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ceid.model.service.OutCityService;
import com.ceid.model.service.RentalService;
import com.ceid.model.service.Service;
import com.ceid.model.service.TaxiService;
import com.ceid.model.transport.Bicycle;
import com.ceid.model.transport.CityCar;
import com.ceid.model.transport.Motorcycle;
import com.ceid.model.transport.OutCityCar;
import com.ceid.model.transport.OutCityTransport;
import com.ceid.model.transport.Rental;

public class HistoryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener
{
	private Context context;
	private ArrayList<Service> dataList;
	private boolean clickable;

	private static final int TAXI = 0;
	private static final int RENTAL = 1;
	private static final int OUTCITY = 2;

	//View Holders
	//====================================================================================================================

	public class RentalViewHolder extends RecyclerView.ViewHolder
	{
		private ImageView icon;
		private TextView titleField;
		private TextView dateField;
		private TextView idField;
		private TextView amountField;

		public RentalViewHolder(View view) {
			super(view);

			icon = view.findViewById(R.id.icon);
			titleField = view.findViewById(R.id.titleField);
			dateField = view.findViewById(R.id.dateField);
			idField = view.findViewById(R.id.idField);
			amountField = view.findViewById(R.id.amountField);
		}
	}

	public class OutCityViewHolder extends RecyclerView.ViewHolder
	{
		private ImageView icon;
		private TextView titleField;
		private TextView dateField;
		private TextView idField;
		private TextView amountField;
		private TextView garageNameField;
		private TextView garageIdField;

		public OutCityViewHolder(View view) {
			super(view);

			icon = view.findViewById(R.id.icon);
			titleField = view.findViewById(R.id.titleField);
			dateField = view.findViewById(R.id.dateField);
			idField = view.findViewById(R.id.idField);
			amountField = view.findViewById(R.id.amountField);
			garageNameField = view.findViewById(R.id.garageNameField);
			garageIdField = view.findViewById(R.id.garageIdField);
		}
	}

	public class TaxiViewHolder extends RecyclerView.ViewHolder
	{
		private ImageView icon;
		private TextView titleField;
		private TextView dateField;
		private TextView idField;
		private TextView amountField;
		private TextView fromField;
		private TextView toField;

		public TaxiViewHolder(View view) {
			super(view);

			icon = view.findViewById(R.id.icon);
			titleField = view.findViewById(R.id.titleField);
			dateField = view.findViewById(R.id.dateField);
			idField = view.findViewById(R.id.idField);
			amountField = view.findViewById(R.id.amountField);
			fromField = view.findViewById(R.id.fromField);
			toField = view.findViewById(R.id.toField);
		}
	}

	//====================================================================================================================


	public HistoryListAdapter(Context context, ArrayList<Service> dataList)
	{
		this.context = context;
		this.dataList = dataList;
		this.clickable = true;
	}

	public HistoryListAdapter(Context context, Service data)
	{
		this.context = context;
		this.dataList = new ArrayList<>();
		this.dataList.add(data);
		this.clickable = false;
	}

	@Override
	public int getItemViewType(int position)
	{
		Service item = dataList.get(position);

		if (item instanceof RentalService)
			return RENTAL;
		else if (item instanceof OutCityService)
			return OUTCITY;
		else
			return TAXI;
	}

	@NonNull
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		if (viewType == OUTCITY)
		{
			View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_list_outcity_item, parent, false);
			return new OutCityViewHolder(itemView);
		}
		else if (viewType == TAXI)
		{
			View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_list_taxi_item, parent, false);
			return new TaxiViewHolder(itemView);
		}
		else
		{
			View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_list_rental_item, parent, false);
			return new RentalViewHolder(itemView);
		}
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
	{
		if (holder instanceof RentalViewHolder)
		{
			RentalService service = (RentalService) dataList.get(position);
			Rental rental = (Rental) service.getTransport();

			int iconId;

			if (rental instanceof CityCar)
				iconId = R.drawable.in_city_car;
			else if (rental instanceof Motorcycle)
				iconId = R.drawable.in_city_motorcycle;
			else if (rental instanceof Bicycle)
				iconId = R.drawable.in_city_bicycle;
			else
				iconId = R.drawable.in_city_scooter;

			((RentalViewHolder) holder).icon.setImageResource(iconId);
			((RentalViewHolder) holder).titleField.setText("Rental: " + rental.getTitle());
			((RentalViewHolder) holder).idField.setText("ID: " + String.format("%d", service.getId()));
			((RentalViewHolder) holder).amountField.setText("Amount: " + service.getPayment().toString());
			((RentalViewHolder) holder).dateField.setText("Date: " + service.getCreationDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
		}
		else if (holder instanceof OutCityViewHolder)
		{
			OutCityService service = (OutCityService) dataList.get(position);
			OutCityTransport transport = (OutCityTransport) service.getTransport();

			int iconId;

			if (transport instanceof OutCityCar)
				iconId = R.drawable.out_city_car;
			else
				iconId = R.drawable.out_city_van;

			((OutCityViewHolder) holder).icon.setImageResource(iconId);
			((OutCityViewHolder) holder).titleField.setText("Short Trip: " + transport.getTitle());
			((OutCityViewHolder) holder).idField.setText("ID: " + String.format("%d", service.getId()));
			((OutCityViewHolder) holder).amountField.setText("Amount: " + service.getPayment().toString());
			((OutCityViewHolder) holder).dateField.setText("Date: " + service.getCreationDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
			((OutCityViewHolder) holder).garageIdField.setText("Garage ID: " + service.getGarageId());
			((OutCityViewHolder) holder).garageNameField.setText("Garage: " + service.getGarageName());
		}
		else if (holder instanceof TaxiViewHolder)
		{
			TaxiService service = (TaxiService) dataList.get(position);

			int iconId = R.drawable.in_city_taxi;

			((TaxiViewHolder) holder).icon.setImageResource(iconId);
			((TaxiViewHolder) holder).titleField.setText("Taxi Ride");
			((TaxiViewHolder) holder).idField.setText("ID: " + String.format("%d", service.getId()));
			((TaxiViewHolder) holder).amountField.setText("Amount: " + service.getPayment().toString());
			((TaxiViewHolder) holder).dateField.setText("Date: " + service.getCreationDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
			//((TaxiViewHolder) holder).fromField.setText("From: " + service.getRequest().getPickupLocation().toString());
			//((TaxiViewHolder) holder).toField.setText("To: " + service.getRequest().getDestination().toString());
		}

		holder.itemView.setOnClickListener(this);
		holder.itemView.setTag(position);
	}

	@Override
	public void onClick(View view)
	{
		if (this.clickable)
		{
			Service service = dataList.get((int)view.getTag());

			Intent intent = new Intent(context, RatingScreen.class);
			intent.putExtra("service", service);
			context.startActivity(intent);
		}
	}

	@Override
	public int getItemCount()
	{
		return dataList.size();
	}
}
