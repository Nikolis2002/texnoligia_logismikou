package com.ceid.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.ceid.model.users.Customer;
import com.ceid.model.users.User;

public class MainScreenFragment extends Fragment
{
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.main_screen_fragment, container, false);
		Customer customer=(Customer) User.getCurrentUser();
		((TextView)view.findViewById(R.id.welcomeText)).setText(String.format("Welcome, %s", customer.getName()));
		return view;
	}
}
