package com.ceid.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RatingBar;

import com.ceid.model.service.OutCityService;
import com.ceid.model.service.Rating;
import com.ceid.model.service.RatingType;
import com.ceid.model.service.RentalService;
import com.ceid.model.service.Service;
import com.ceid.model.service.TaxiService;
import com.ceid.model.transport.Taxi;
import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;

public class RatingScreen extends AppCompatActivity
{
	Service service;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rating_screen);

		this.service = (Service) getIntent().getExtras().get("service");

		RecyclerView list = findViewById(R.id.list);
		list.setLayoutManager(new LinearLayoutManager(this));
		list.setAdapter(new HistoryListAdapter(this, service));

		Bundle bundle = new Bundle();
		bundle.putSerializable("rating", service.getRating());

		if (service instanceof RentalService)
		{
			Fragment fragment = new RatingRental();
			fragment.setArguments(bundle);
			getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
		}
		else if (service instanceof OutCityService)
		{
			Fragment fragment = new RatingOutcity();
			fragment.setArguments(bundle);
			getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
		}
		else if (service instanceof TaxiService)
		{
			Fragment fragment = new RatingTaxi();
			fragment.setArguments(bundle);
			getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
		}
	}

	public boolean validateRating(Float r1, Float r2, String text)
	{

		if ((r1 <= 0) || (r1 > 5))
		{
			return false;
		}

		if ((r2 != null) && ((r2 < 0) || (r2 > 5)))
		{
			return false;
		}

		if (text.length() > 150)
		{
			//error
			return false;
		}

		return true;
	}

	private void saveRating(Float r1, Float r2, String text)
	{
		Rating rating = service.rate(r1, r2, text);

		//Save to database
	}

	public void submit(View view)
	{
		Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
		View fview = fragment.getView();

		Float r1;
		Float r2;
		String txt;

		if (service instanceof RentalService)
		{
			RatingBar vehicle = fview.findViewById(R.id.vehicleRating);
			TextInputEditText text = fview.findViewById(R.id.commentText);

			vehicle.setRating(5.0f);

			r1 = vehicle.getRating();
			r2  = null;
			txt = text.getText().toString();
		}
		else if (service instanceof OutCityService)
		{
			RatingBar vehicle = fview.findViewById(R.id.vehicleRating);
			RatingBar garage = fview.findViewById(R.id.garageRating);
			TextInputEditText text = fview.findViewById(R.id.commentText);

			r1 = vehicle.getRating();
			r2  = garage.getRating();
			txt = text.getText().toString();
		}
		else
		{
			RatingBar taxi = fview.findViewById(R.id.taxiRating);
			RatingBar driver = fview.findViewById(R.id.driverRating);
			TextInputEditText text = fview.findViewById(R.id.commentText);

			r1 = taxi.getRating();
			r2  = driver.getRating();
			txt = text.getText().toString();
		}

		if (validateRating(r1, r2, txt))
			saveRating(r1, r2, txt);
	}
}
