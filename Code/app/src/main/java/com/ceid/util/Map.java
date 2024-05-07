package com.ceid.util;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;

import com.ceid.ui.ScrollMapFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

public class Map implements OnMapReadyCallback
{
	private GoogleMap gmap;

	private NestedScrollView scrollView; //NestedScrollView or ConstraintLayout
	private SupportMapFragment mapFragment; //SupportMapFragment or ScrollMapFragment

	private boolean clickable;
	private Coordinates pinCoords = null;

	private MapWrapperReadyListener listener;

	//We need 2 things for the map
	//mapFragment is the fragment where the map is drawn
	//	It can be SupportMapFragment, or a ScrollMapFragment (subclass) if the map is inside a scroll view
	//scrollView is the top-level view of the page
	//	Usually, it is ConstraintLayout. In this case, we don't need it (null)
	//	If it's NestedScrollView, we need it
	//WHY ALL THIS???
	//If we have a scrollview, we need to override default behavior of the page, because scrolling prevents us from using the map
	public Map(SupportMapFragment mapFragment, NestedScrollView scrollView)
	{
		this.scrollView = scrollView;
		this.mapFragment = mapFragment;
		this.clickable = false;

		assert mapFragment != null;
		mapFragment.getMapAsync((OnMapReadyCallback)this);
	}

	@Override
	public void onMapReady(@NonNull GoogleMap googleMap)
	{
		this.gmap = googleMap;

		//Defaults
		this.gmap.getUiSettings().setRotateGesturesEnabled(false);

		if (listener != null)
			listener.onMapWrapperReady();

		//Only for maps within a scrollView
		if (scrollView != null)
		{
			((ScrollMapFragment)mapFragment).setListener(new ScrollMapFragment.OnTouchListener()
			{
				@Override
				public void onTouch()
				{
					scrollView.requestDisallowInterceptTouchEvent(true);
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
					placePin(new Coordinates(latLng));
				}
			}
		});
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

	public void placePin(Coordinates coords)
	{
		gmap.clear();
		gmap.addMarker(new MarkerOptions().position(coords.toLatLng()));
		pinCoords = coords;
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
}
