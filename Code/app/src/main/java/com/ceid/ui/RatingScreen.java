package com.ceid.ui;

import android.os.Bundle;
import android.view.WindowManager;

import com.ceid.model.service.OutCityService;
import com.ceid.model.service.RentalService;
import com.ceid.model.service.Service;
import com.ceid.model.service.TaxiService;
import com.ceid.model.transport.Taxi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;

public class RatingScreen extends AppCompatActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rating_screen);

		Service service = (Service) getIntent().getExtras().get("service");

		RecyclerView list = findViewById(R.id.list);
		list.setLayoutManager(new LinearLayoutManager(this));
		list.setAdapter(new HistoryListAdapter(this, service));

		if (service instanceof RentalService)
		{
			getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new RatingRental()).commit();
		}
		else if (service instanceof OutCityService)
		{
			getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new RatingOutcity()).commit();
		}
		else if (service instanceof TaxiService)
		{
			getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new RatingTaxi()).commit();
		}
	}
}
