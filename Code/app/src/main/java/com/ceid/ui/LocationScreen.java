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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;

public class LocationScreen extends AppCompatActivity implements MapWrapperReadyListener
{
	private Map map;
	private Bundle data;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.location_screen);

		//Initialize map
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.locationMapView);

		map = new Map(mapFragment, this);
		map.setClickable(true);
		map.setListener(this);

		//See if we had already inserted coordinates before
		this.data = getIntent().getExtras();
	}

	//User presses the button to confirm location
	public void onSubmit(View view)
	{
		data = new Bundle();
		data.putSerializable("coords", map.getPinCoords());
		data.putFloat("zoom", map.getZoom());

		Log.d("LOCATION", String.valueOf(map.getZoom()));

		Intent res = new Intent();
		res.putExtras(data);
		setResult(Activity.RESULT_OK, res);

		finish();
	}

	public void onMapWrapperReady()
	{
		if (data != null)
		{
			Coordinates coords = (Coordinates) data.getSerializable("coords");

			if (coords != null)
			{
				map.placePin(coords, true);
				map.setZoom(data.getFloat("zoom"));
				map.setPosition(coords);
			}
		}
	}
}
