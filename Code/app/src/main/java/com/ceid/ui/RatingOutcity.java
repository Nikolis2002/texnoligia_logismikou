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

public class RatingOutcity extends Fragment
{
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

		View view = inflater.inflate(R.layout.rating_outcity, container, false);

		Bundle args = getArguments();
		Rating rating = (Rating)args.getSerializable("rating");

		if (rating != null)
		{
			RatingBar vehicleBar = view.findViewById(R.id.vehicleRating);
			RatingBar garageBar = view.findViewById(R.id.garageRating);
			TextInputEditText comment = view.findViewById(R.id.commentText);

			vehicleBar.setRating(rating.getVehicleStars());
			vehicleBar.setIsIndicator(true);

			garageBar.setRating(rating.getGarageStars());
			garageBar.setIsIndicator(true);

			comment.setText(rating.getComment());
			comment.setFocusableInTouchMode(false);
			comment.clearFocus();
		}

		return view;
	}
}

