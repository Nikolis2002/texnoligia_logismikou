package com.ceid.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class RouteHistory extends AppCompatActivity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.route_history);

		//Bottom navigation
		BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
		bottomNavigationView.setSelectedItemId(R.id.page_history);

		bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {

			@Override
			public boolean onNavigationItemSelected(@NonNull MenuItem item)
			{

				int id = item.getItemId();

				if (id == R.id.page_home)
				{

				}
				else if (id == R.id.page_in_city)
				{
					Intent intent = new Intent(RouteHistory.this, MainScreen.class);
					startActivity(intent);
				}
				else if (id == R.id.page_out_city)
				{
				}
				else if (id == R.id.page_history)
				{
				}
				else if (id == R.id.page_profile)
				{
				}

				return true; //successfully handled
			}
		});
	}
}
