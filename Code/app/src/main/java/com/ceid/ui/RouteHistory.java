package com.ceid.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ceid.model.payment_methods.Currency;
import com.ceid.model.payment_methods.Payment;
import com.ceid.model.service.RentalService;
import com.ceid.model.service.Service;
import com.ceid.model.transport.CityCar;
import com.ceid.util.Coordinates;
import com.ceid.util.PositiveInteger;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class RouteHistory extends Fragment
{
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.route_history, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);

		ArrayList<Service> data = new ArrayList<>();
		data.add(new RentalService(
				1,
				LocalDateTime.now(),
				new Payment(new Currency(87), Payment.Method.WALLET),
				null,
				new CityCar(
						"ABC-1234",
						true,
						0,
						"MONDEO",
						"FORD",
						"1993",
						null,
						new Currency(1.40),
						new Coordinates(38.2442870,21.7326153),
						new PositiveInteger(0)
				)
		));

		RecyclerView list = view.findViewById(R.id.list);
		list.setLayoutManager(new LinearLayoutManager(getContext()));
		list.setAdapter(new HistoryListAdapter(getContext(), data));
	}
}
