package com.ceid.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import com.ceid.util.Coordinates;
import com.ceid.util.Map;
import com.ceid.util.MapWrapperReadyListener;
import com.google.android.gms.maps.SupportMapFragment;

public class LocationScreen extends AppCompatActivity implements MapWrapperReadyListener
{
	private Map map;
	private Coordinates coords;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.location_screen);

		//Initialize map
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.locationMapView);

		map = new Map(mapFragment, null);
		map.setClickable(true);
		map.setListener(this);

		//See if we had already inserted coordinates before
		this.coords = (Coordinates) getIntent().getSerializableExtra("coords");

		if (coords != null)
			Log.d("LOCATION", coords.toString());
		else
			Log.d("LOCATION", "NULL");
	}

	//User presses the button to confirm location
	public void onSubmit(View view)
	{
		this.coords = map.getPinCoords();

		Intent res = new Intent();
		res.putExtra("coords", coords);
		setResult(Activity.RESULT_OK, res);

		finish();
	}

	public void onMapWrapperReady()
	{
		Log.d("LOCATION", "READY");

		if (coords != null)
			map.placePin(coords);
	}
}
