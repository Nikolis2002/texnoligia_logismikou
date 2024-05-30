package com.ceid.ui;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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

		View view = inflater.inflate(R.layout.rating_rental, container, false);

		Bundle args = getArguments();
		Rating rating = (Rating)args.getSerializable("rating");

		if (rating != null)
			showRating(view, rating);

		return view;
	}

	public void showRating(View view, Rating rating)
	{
		RatingBar vehicleBar = view.findViewById(R.id.vehicleRating);
		TextInputEditText comment = view.findViewById(R.id.commentText);

		vehicleBar.setRating(rating.getVehicleStars());
		vehicleBar.setIsIndicator(true);

		comment.setText(rating.getComment());
		comment.setFocusableInTouchMode(false);
		comment.clearFocus();
	}
}
