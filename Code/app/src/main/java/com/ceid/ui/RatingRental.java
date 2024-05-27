package com.ceid.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.ceid.model.service.OutCityService;
import com.ceid.model.service.Rating;
import com.ceid.model.service.RentalService;
import com.ceid.model.service.TaxiService;
import com.google.android.material.textfield.TextInputEditText;

public class RatingRental extends Fragment
{
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		return inflater.inflate(R.layout.rating_rental, container, false);
	}

	public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);

		Bundle args = getArguments();

		Rating rating = (Rating)args.get("rating");
		RatingBar vehicleBar = view.findViewById(R.id.vehicleRating);

		vehicleBar.setRating(5.0f);

		if (rating != null)
		{
			RatingBar vefhicleBar = view.findViewById(R.id.vehicleRating);
			TextInputEditText comment = view.findViewById(R.id.commentText);

			vefhicleBar.setRating(5.0f);
			//vehicleBar.setEnabled(false);
			comment.setText(rating.getComment());
			//comment.setEnabled(false);

		}
	}
}
