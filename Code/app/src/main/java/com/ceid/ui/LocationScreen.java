package com.ceid.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import com.ceid.util.Coordinates;
import com.ceid.util.Map;
import com.ceid.util.MapWrapperReadyListener;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;
import java.util.Arrays;

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

		map.addPolygon(new ArrayList<>(Arrays.asList(
				new Coordinates(38.1943262,21.6946574),
				new Coordinates(38.2141890,21.7176181),
				new Coordinates(38.2446701,21.7267309),
				new Coordinates(38.2505200,21.7355607),
				new Coordinates(38.2694717,21.7392819),
				new Coordinates(38.2818358,21.7473017),
				new Coordinates(38.2802243,21.7543982),
				new Coordinates(38.3113186,21.7811945),
				new Coordinates(38.3087241,21.7869304),
				new Coordinates(38.3190723,21.8154114),
				new Coordinates(38.3130451,21.8226795),
				new Coordinates(38.2800458,21.7920839),
				new Coordinates(38.2597798,21.7661362),
				new Coordinates(38.2344487,21.7740222),
				new Coordinates(38.2064027,21.7901189),
				new Coordinates(38.1871228,21.7655592),
				new Coordinates(38.1822601,21.7208944),
				new Coordinates(38.1943262,21.6946574)
		)));

		map.addPolygon(new ArrayList<>(Arrays.asList(
				new Coordinates(37.9594493,23.6064075),
				new Coordinates(38.1013615,23.7380416),
				new Coordinates(38.0506260,23.8690360),
				new Coordinates(37.9020807,23.7297764),
				new Coordinates(37.9594493,23.6064075)
		)));

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
	public void confirmLocation(View view)
	{
		if (map.withinPolygon())
		{
			data = new Bundle();
			data.putSerializable("coords", map.getPinCoords());
			data.putFloat("zoom", map.getZoom());
			data.putSerializable("polygon", map.getSelectedPolygonCoords());

			Log.d("LOCATION", String.valueOf(map.getZoom()));

			Intent res = new Intent();
			res.putExtras(data);
			setResult(Activity.RESULT_OK, res);

			finish();
		}
		else
		{
			Toast.makeText(this, "Coordinates are out of coverage area", Toast.LENGTH_SHORT).show();
		}
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
