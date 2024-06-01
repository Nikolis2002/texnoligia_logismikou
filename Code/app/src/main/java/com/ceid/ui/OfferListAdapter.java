package com.ceid.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ceid.model.service.Coupon;
import com.ceid.util.DateFormat;

import java.util.ArrayList;

public class OfferListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
	private Context context;
	private ArrayList<Coupon> dataList;
	private View.OnClickListener listener;

	//View Holders
	//====================================================================================================================

	public class MyViewHolder extends RecyclerView.ViewHolder
	{
		private ImageView icon;
		private TextView nameField;
		private TextView dateField;
		private TextView amountField;
		private TextView pointsField;
		private TextView supplyField;

		public MyViewHolder(View view)
		{
			super(view);

			icon = view.findViewById(R.id.icon);
			nameField = view.findViewById(R.id.nameField);
			dateField = view.findViewById(R.id.dateField);
			amountField = view.findViewById(R.id.amountField);
			pointsField = view.findViewById(R.id.pointsField);
			supplyField = view.findViewById(R.id.supplyField);
		}
	}

	//====================================================================================================================


	public OfferListAdapter(Context context, ArrayList<Coupon> dataList, View.OnClickListener listener)
	{
		this.context = context;
		this.dataList = dataList;
		this.listener = listener;
	}

	public OfferListAdapter(Context context, Coupon data)
	{
		this.context = context;
		this.dataList = new ArrayList<>();
		this.dataList.add(data);
	}

	@NonNull
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_list_item, parent, false);
		return new MyViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
	{
		Coupon coupon = dataList.get(position);

		((MyViewHolder) holder).icon.setImageResource(R.drawable.coupon2);
		((MyViewHolder) holder).nameField.setText(coupon.getName());
		((MyViewHolder) holder).pointsField.setText(String.format("Points: %d", coupon.getPoints()));
		((MyViewHolder) holder).amountField.setText(String.format("Value: %.02f€", coupon.getMoney()));
		((MyViewHolder) holder).dateField.setText("Valid until: " + DateFormat.humanReadable(coupon.getExpirationDate()));

		if (coupon.limited())
			((MyViewHolder) holder).supplyField.setText(String.format("Only %d left!", coupon.getSupply()));
		else
			((MyViewHolder) holder).supplyField.setText("");

		holder.itemView.setOnClickListener(listener);
		holder.itemView.setTag(position);
	}

	@Override
	public int getItemCount()
	{
		return dataList.size();
	}

	public Coupon getItem(int position)
	{
		return dataList.get(position);
	}

	public void remove(int position)
	{
		dataList.remove(position);
	}
}
