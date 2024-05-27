package com.ceid.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.ceid.model.service.Rating;
import com.google.android.material.textfield.TextInputEditText;

public class RatingTaxi extends Fragment
{
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

		View view = inflater.inflate(R.layout.rating_taxi, container, false);

		Bundle args = getArguments();
		Rating rating = (Rating)args.getSerializable("rating");

		if (rating != null)
		{
			RatingBar vehicleBar = view.findViewById(R.id.taxiRating);
			RatingBar driverBar = view.findViewById(R.id.driverRating);
			TextInputEditText comment = view.findViewById(R.id.commentText);

			vehicleBar.setRating(rating.getVehicleStars());
			vehicleBar.setIsIndicator(true);

			driverBar.setRating(rating.getDriverStars());
			driverBar.setIsIndicator(true);

			comment.setText(rating.getComment());
			comment.setFocusableInTouchMode(false);
			comment.clearFocus();
		}

		return view;
	}
}

