package com.ceid.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.ceid.model.users.Customer;
import com.ceid.model.users.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class CustomerProfile extends Fragment
{
	private TextView username,pass,name,surname,email,license,points,wallet,payment;
	private Customer customer;

    @Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.customer_profile, container, false);
        User user = User.getCurrentUser();
		customer=(Customer) user;

		username= view.findViewById(R.id.user_name);
		name=view.findViewById(R.id.name);
		surname=view.findViewById(R.id.surname);
		email=view.findViewById(R.id.email);
		license=view.findViewById(R.id.license);
		points=view.findViewById(R.id.points);
		wallet=view.findViewById(R.id.wallet);

		loadFields();

		return view;
	}

	public void loadFields()
	{
		username.setText(customer.getUsername());
		name.setText(customer.getName());
		surname.setText(customer.getLname());
		email.setText(customer.getEmail());
		license.setText(customer.getLicense());
		points.setText(String.valueOf(customer.getPoints().getPoints()));
		wallet.setText(String.format("%.02f€", customer.getWallet().getBalance()));
	}


	public void onResume()
	{
		super.onResume();
		loadFields();
	}
}
