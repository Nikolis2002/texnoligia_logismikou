package com.ceid.util;

import androidx.core.widget.NestedScrollView;

import com.ceid.ui.ScrollMapFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

public class Map implements OnMapReadyCallback
{
	private GoogleMap gmap;
	private NestedScrollView scrollView;
	private ScrollMapFragment mapFragment;

	public Map(ScrollMapFragment mapFragment, NestedScrollView scrollView)
	{
		this.scrollView = scrollView;
		this.mapFragment = mapFragment;

		assert mapFragment != null;
		mapFragment.getMapAsync((OnMapReadyCallback)this);
	}

	@Override
	public void onMapReady(GoogleMap googleMap)
	{
		gmap = googleMap;

		mapFragment.setListener(new ScrollMapFragment.OnTouchListener()
		{
			@Override
			public void onTouch()
			{
				scrollView.requestDisallowInterceptTouchEvent(true);
			}
		});
	}
}
