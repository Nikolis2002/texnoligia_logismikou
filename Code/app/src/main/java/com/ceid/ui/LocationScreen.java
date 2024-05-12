package com.ceid.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.TextView;

import com.ceid.util.Coordinates;
import com.ceid.util.Map;
import com.ceid.util.MapWrapperReadyListener;
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
		taxiScreenCheck();
	}
	
	private void taxiScreenCheck(){

		Intent taxi = getIntent();

		if(taxi.hasExtra("text")){
			String text = this.data.getString("text");
			TextView textview = findViewById(R.id.textView);
			textview.setText(text);
		}
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
		Coordinates Patra = new Coordinates( 38.246639, 21.734573);
		map.setZoom(12);
		map.setPosition(Patra);

		if (data != null)
		{
			Coordinates coords = (Coordinates) data.getSerializable("coords");
			Coordinates starCoords = (Coordinates) data.getSerializable("location");

			if (coords != null)
			{
				map.placePin(coords, false);
				map.setZoom(data.getFloat("zoom"));
				map.setPosition(coords);
			}

			if(starCoords!=null){
				map.placeStartPin(starCoords, false, R.drawable.emoji_people);
			}
		}
	}
}
