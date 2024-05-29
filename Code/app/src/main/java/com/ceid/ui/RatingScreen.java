package com.ceid.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ceid.Network.ApiClient;
import com.ceid.Network.ApiService;
import com.ceid.Network.jsonStringParser;
import com.ceid.model.service.OutCityService;
import com.ceid.model.service.Rating;
import com.ceid.model.service.RatingType;
import com.ceid.model.service.RentalService;
import com.ceid.model.service.Service;
import com.ceid.model.service.TaxiService;
import com.ceid.model.transport.Taxi;
import com.ceid.model.users.User;
import com.google.android.material.textfield.TextInputEditText;
import com.ceid.model.users.Customer;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RatingScreen extends AppCompatActivity
{
	private int servicePos;
	private Service service;
	private Customer customer;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rating_screen);

		OnBackPressedDispatcher dispatcher = getOnBackPressedDispatcher();
		dispatcher.addCallback(this, new OnBackPressedCallback(true) {
			@Override
			public void handleOnBackPressed() {
				finish();
			}
		});

		this.servicePos = getIntent().getIntExtra("service_pos", -1);
		this.customer = (Customer) User.getCurrentUser();
		this.service = customer.getHistory().getList().get(servicePos);

		RecyclerView list = findViewById(R.id.list);
		list.setLayoutManager(new LinearLayoutManager(this));
		list.setAdapter(new HistoryListAdapter(this, service));

		Bundle bundle = new Bundle();
		bundle.putSerializable("rating", service.getRating());

		if (service instanceof RentalService)
		{
			RatingRental fragment = new RatingRental();
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

		if (service.getRating() != null)
		{
			findViewById(R.id.submitBtn).setVisibility(View.GONE);
			((TextView)findViewById(R.id.ratingText)).setText("Your rating");
		}
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
		{
			saveRating(r1, r2, txt);
		}
	}

	public boolean validateRating(Float r1, Float r2, String text)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});


		//Vehicle stars out of range
		if ((r1 <= 0) || (r1 > 5))
		{
			builder.setTitle("Star Error");
			builder.setMessage("Vehicle stars are not within allowed range (1 to 5)");
			AlertDialog alert = builder.create();
			alert.setCanceledOnTouchOutside(false);
			alert.show();

			return false;
		}

		//Driver/ Garage stars out of range
		if ((r2 != null) && ((r2 <= 0) || (r2 > 5)))
		{
			builder.setTitle("Star Error");
			builder.setMessage(String.format("%s stars are not within allowed range (1 to 5)", service instanceof OutCityService ? "Garage" : "Driver"));
			AlertDialog alert = builder.create();
			alert.setCanceledOnTouchOutside(false);
			alert.show();

			return false;
		}

		if (text.length() > 200)
		{
			builder.setTitle("Achievement Unlocked - Professional Yapper");
			builder.setMessage("Comment is too big, must be at most 200 characters long");
			AlertDialog alert = builder.create();
			alert.setCanceledOnTouchOutside(false);
			alert.show();

			return false;
		}

		return true;
	}

	private void saveRating(Float r1, Float r2, String text)
	{
		Rating rating = service.rate(r1, r2, text);

		//Parameters
		//===========================================================================
		List<Map<String,Object>> values = new ArrayList<>();
		java.util.Map<String, Object> params = new LinkedHashMap<>();

		params.put("service_id", service.getId());
		params.put("vehicle_stars", Math.round(r1));

		if (!(service instanceof RentalService))
		{
			params.put("other_stars", Math.round(r2));
		}

		params.put("comment", text);

		values.add(params);

		//Procedure name
		//===========================================================================
		String procedure;

		if (service instanceof TaxiService)
			procedure = "rate_taxi_service";
		else if (service instanceof OutCityService)
			procedure = "rate_out_city_service";
		else
			procedure = "rate_rental_service";

		//Save to the database
		//===========================================================================

		String jsonString = jsonStringParser.createJsonString(procedure, values);

		ApiService api = ApiClient.getApiService();
		Call<ResponseBody> call = api.getFunction(jsonString);

		call.enqueue(new Callback<ResponseBody>()
		{
			@Override
			public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

				if(response.isSuccessful())
				{
					//Success message
					Toast.makeText(getApplicationContext(), "Rating submitted successfully", Toast.LENGTH_LONG).show();
					finish();

				}
				else
				{
					//Failure message
					Toast.makeText(getApplicationContext(), "Failed to save rating in the server", Toast.LENGTH_LONG).show();
					finish();
				}

			}
			@Override
			public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {
				//Failure message
				Toast.makeText(getApplicationContext(), "Failed to reach server", Toast.LENGTH_LONG).show();
				finish();
			}
		});
	}

}
