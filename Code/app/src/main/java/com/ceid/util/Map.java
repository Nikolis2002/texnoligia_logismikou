package com.ceid.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
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
	private Coordinates startCoords;
	private GoogleMap.OnMarkerClickListener markerListener;
	private GoogleMap.OnMapClickListener clickListener;
	private Marker clickedMarker;

	private MapWrapperReadyListener listener;

	//We need 2 things for the map
	//mapFragment is the fragment where the map is drawn
	//	It can be SupportMapFragment, or a ScrollMapFragment (subclass) if the map is inside a scroll view
	//scrollView is the top-level view of the page
	//	Usually, it is ConstraintLayout. In this case, we don't need it (null)
	//	If it's NestedScrollView, we need it
	//WHY ALL THIS???
	//If we have a scrollview, we need to override default behavior of the page, because scrolling prevents us from using the map

	public Map(SupportMapFragment mapFragment, Context context)
	{
		this.view = ((Activity) context).findViewById(android.R.id.content);
		this.mapFragment = mapFragment;
		this.clickable = false;
		this.markerListener = null;
		this.clickedMarker = null;
		this.clickListener = null;

		assert mapFragment != null;
		mapFragment.getMapAsync((OnMapReadyCallback)this);
	}

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

		//Initialize clicking
		//THIS IS THE DEFAULT CLICKER. YOU CAN ASSIGN OTHER CLICK HANDLER IF YOU WANT
		gmap.setOnMapClickListener(new GoogleMap.OnMapClickListener()
		{
			@Override
			public void onMapClick(@NonNull LatLng latLng)
			{
				if (clickable)
				{
					if (clickedMarker == null)
						clickedMarker = placePin(new Coordinates(latLng), false);
					else
					{
						clickedMarker.setPosition(latLng);
						pinCoords = new Coordinates(latLng);
					}
				}
			}
		});

		//Set listeners if we have previously called setMarkerListener/ setClickListener
		this.gmap.setOnMarkerClickListener(markerListener);

		if (clickListener != null)
			this.gmap.setOnMapClickListener(clickListener);

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

		clickedMarker = gmap.addMarker(new MarkerOptions().position(coords.toLatLng()));
		pinCoords = coords;

		return clickedMarker;
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

		clickedMarker = gmap.addMarker(opt);

		pinCoords = coords;

		return clickedMarker;
	}

	public Marker placePin(Coordinates coords, boolean clear, int iconId, boolean draggable)
	{
		if (clear)
			gmap.clear();

		Bitmap b = BitmapFactory.decodeResource(mapFragment.getResources(), iconId);
		Bitmap smallMarker = Bitmap.createScaledBitmap(b, 128, 128, false);

		MarkerOptions opt = new MarkerOptions();
		opt.draggable(draggable);
		opt.position(coords.toLatLng());
		opt.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

		clickedMarker = gmap.addMarker(opt);

		pinCoords = coords;

		return clickedMarker;
	}

	public void placeStartPin(Coordinates coords, boolean clear, int iconId)
	{
		if (clear)
			gmap.clear();

		Bitmap b = BitmapFactory.decodeResource(mapFragment.getResources(), iconId);
		Bitmap smallMarker = Bitmap.createScaledBitmap(b, 100, 100, false);
		startCoords=coords;
		MarkerOptions opt = new MarkerOptions();
		opt.position(coords.toLatLng());
		opt.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
		opt.title("Your Location");

		gmap.addMarker(opt);
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

	public void smoothTransition(Coordinates coords, float zoom)
	{
		gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(coords.toLatLng(), zoom));
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

	public void setClickListener(GoogleMap.OnMapClickListener listener)
	{
		this.clickListener = listener;

		if (this.gmap != null)
		{
			this.gmap.setOnMapClickListener(listener);
		}
	}
}
