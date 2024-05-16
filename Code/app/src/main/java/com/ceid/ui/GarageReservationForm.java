package com.ceid.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.ceid.model.transport.CityCar;
import com.ceid.model.transport.Garage;
import com.ceid.model.transport.OutCityCar;
import com.ceid.model.transport.OutCityTransport;
import com.ceid.model.transport.Van;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.ArrayList;

public class GarageReservationForm extends AppCompatActivity
{
	private OutCityTransport vehicle;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.garage_reservation_form);

		this.vehicle = (OutCityTransport) getIntent().getExtras().getSerializable("vehicle");
		assert vehicle != null;

		//Text

		TextView title = findViewById(R.id.textTitle);
		TextView rate = findViewById(R.id.textRate);
		TextView seats = findViewById(R.id.textSeats);
		TextView plate = findViewById(R.id.textPlate);

		title.setText(String.format("%s %s (%s)", vehicle.getManufacturer(), vehicle.getModel(), vehicle.getManufYear()));
		rate.setText(String.format("%s/day", vehicle.getRate().toString()));
		seats.setText(String.format("%d", vehicle.getSeats()));
		plate.setText(String.format("%s", vehicle.getLicensePlate()));

		//Icon

		ImageView img = findViewById(R.id.imageView);

		if (vehicle instanceof OutCityCar)
			img.setImageResource(R.drawable.out_city_car);
		else if (vehicle instanceof Van)
			img.setImageResource(R.drawable.out_city_van);

		/*
		MaterialTimePicker picker = new MaterialTimePicker.Builder()
				.setTimeFormat(TimeFormat.CLOCK_12H)
				.setHour(12)
				.setMinute(10)
				.setTitleText("Select Appointment time")
				.build();

		picker.show(getSupportFragmentManager(), "tag");*/
	}

	public void back(View view)
	{
		finish();
	}
}
