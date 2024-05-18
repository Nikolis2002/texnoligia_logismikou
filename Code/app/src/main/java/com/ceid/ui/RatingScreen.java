package com.ceid.ui;

import android.os.Bundle;
import com.ceid.model.service.Service;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;

public class RatingScreen extends AppCompatActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rating_screen);

		Service service = (Service) getIntent().getExtras().get("service");
	}
}
