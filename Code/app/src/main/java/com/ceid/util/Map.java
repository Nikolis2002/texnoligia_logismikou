package com.ceid.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;

import com.ceid.ui.R;
import com.ceid.ui.ScrollMapFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import android.graphics.Bitmap;

public class Map implements OnMapReadyCallback
{
	private GoogleMap gmap;

	private View view; //Usually NestedScrollView or ConstraintLayout
	private SupportMapFragment mapFragment; //SupportMapFragment or ScrollMapFragment

	private boolean clickable;
	private Coordinates pinCoords = null;

	private GoogleMap.OnMarkerClickListener markerListener;

	private MapWrapperReadyListener listener;

	//We need 2 things for the map
	//mapFragment is the fragment where the map is drawn
	//	It can be SupportMapFragment, or a ScrollMapFragment (subclass) if the map is inside a scroll view
	//scrollView is the top-level view of the page
	//	Usually, it is ConstraintLayout. In this case, we don't need it (null)
	//	If it's NestedScrollView, we need it
	//WHY ALL THIS???
	//If we have a scrollview, we need to override default behavior of the page, because scrolling prevents us from using the map

	/*
	public Map(SupportMapFragment mapFragment, NestedScrollView scrollView)
	{
		this.view = scrollView;
		this.mapFragment = mapFragment;
		this.clickable = false;
		this.markerListener = null;

		assert mapFragment != null;
		mapFragment.getMapAsync((OnMapReadyCallback)this);
	}
	*/

	public Map(SupportMapFragment mapFragment, Context context)
	{
		this.view = ((Activity) context).findViewById(android.R.id.content);
		this.mapFragment = mapFragment;
		this.clickable = false;
		this.markerListener = null;

		assert mapFragment != null;
		mapFragment.getMapAsync((OnMapReadyCallback)this);
	}

	/*

	public Map(SupportMapFragment mapFragment, NestedScrollView scrollView, MapWrapperReadyListener listener)
	{
		this(mapFragment, scrollView);
		this.listener = listener;
	}
	*/

	public Map(SupportMapFragment mapFragment, Context context, MapWrapperReadyListener listener)
	{
		this(mapFragment, context);
		this.listener = listener;
	}

	@Override
	public void onMapReady(@NonNull GoogleMap googleMap)
	{
		this.gmap = googleMap;

		//Defaults
		this.gmap.getUiSettings().setRotateGesturesEnabled(false);

		//Set marker listener if we have previously called setMarkerListener
		this.gmap.setOnMarkerClickListener(markerListener);


		//Only for maps within a scrollView
		if (this.view instanceof NestedScrollView)
		{
			((ScrollMapFragment)mapFragment).setListener(new ScrollMapFragment.OnTouchListener()
			{
				@Override
				public void onTouch()
				{
					((NestedScrollView)view).requestDisallowInterceptTouchEvent(true);
				}
			});
		}

		//Initialize clicking
		gmap.setOnMapClickListener(new GoogleMap.OnMapClickListener()
		{
			@Override
			public void onMapClick(@NonNull LatLng latLng)
			{
				if (clickable)
				{
					placePin(new Coordinates(latLng), true);
				}
			}
		});

		if (listener != null)
			listener.onMapWrapperReady();
	}

	public void setListener(MapWrapperReadyListener listener)
	{
		this.listener = listener;
	}

	public void setClickable(boolean clickable)
	{
		this.clickable = clickable;
	}

	public boolean isClickable()
	{
		return clickable;
	}

	public Marker placePin(Coordinates coords, boolean clear)
	{
		if (clear)
			gmap.clear();

		Marker marker = gmap.addMarker(new MarkerOptions().position(coords.toLatLng()));
		pinCoords = coords;

		return marker;
	}

	public Marker placePin(Coordinates coords, boolean clear, int iconId)
	{
		if (clear)
			gmap.clear();

		Bitmap b = BitmapFactory.decodeResource(mapFragment.getResources(), iconId);
		Bitmap smallMarker = Bitmap.createScaledBitmap(b, 128, 128, false);

		MarkerOptions opt = new MarkerOptions();
		opt.position(coords.toLatLng());
		opt.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

		Marker marker = gmap.addMarker(opt);

		pinCoords = coords;

		return marker;
	}

	public GoogleMap getMap()
	{
		return gmap;
	}

	public void setZoom(float zoomLevel)
	{
		gmap.moveCamera(CameraUpdateFactory.zoomTo(zoomLevel));
	}

	public void setPosition(Coordinates coords)
	{
		gmap.moveCamera(CameraUpdateFactory.newLatLng(coords.toLatLng()));
	}

	public void smoothTransition(Coordinates coords)
	{
		gmap.animateCamera(CameraUpdateFactory.newLatLng(coords.toLatLng()));
	}

	public float getZoom()
	{
		return gmap.getCameraPosition().zoom;
	}

	public Coordinates getPinCoords()
	{
		if (pinCoords != null)
			return new Coordinates(pinCoords);
		else return null;
	}

	public void setMarkerListener(GoogleMap.OnMarkerClickListener listener)
	{
		this.markerListener = listener;

		if (this.gmap != null)
		{
			this.gmap.setOnMarkerClickListener(listener);
		}
	}
}
