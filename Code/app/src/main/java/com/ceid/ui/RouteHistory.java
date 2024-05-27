package com.ceid.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import com.ceid.Network.ApiClient;
import com.ceid.Network.ApiService;
import com.ceid.model.payment_methods.Payment;
import com.ceid.model.service.OutCityService;
import com.ceid.model.service.RentalService;
import com.ceid.model.service.Service;
import com.ceid.model.service.TaxiRequest;
import com.ceid.model.service.TaxiService;
import com.ceid.model.transport.CityCar;
import com.ceid.model.transport.Taxi;
import com.ceid.model.transport.Van;
import com.ceid.model.users.Customer;
import com.ceid.model.users.CustomerHistory;
import com.ceid.model.users.User;
import com.ceid.util.Coordinates;
import com.ceid.util.PositiveInteger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

		Customer customer = (Customer) User.getCurrentUser();
		CustomerHistory history = customer.getHistory();

		//History has not been retrieved from the database previously
		if (history == null)
		{
			ApiService api = ApiClient.getApiService();

			Call<ResponseBody> call = api.getHistory(customer.getUsername());

			call.enqueue(new Callback<ResponseBody>(){

				@Override
				public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
				{
					if (response.isSuccessful())
					{
						ArrayList<Service> history = new ArrayList<Service>();

						ArrayNode root;

						try
						{
							String jsonString = response.body().string();
							ObjectMapper mapper = new ObjectMapper();
							root = (ArrayNode) mapper.readTree(jsonString);
						}
						catch (Exception e)
						{
							throw new RuntimeException(e);
						}

						for (JsonNode entry : root)
						{
							String type = entry.get("type").asText();

							if (Objects.equals(type, "rental"))
								history.add(new RentalService(entry));
							else if (Objects.equals(type, "out_city"))
								history.add(new OutCityService(entry));
							else if (Objects.equals(type, "taxi"))
								history.add(new TaxiService(entry));
						}

						customer.setHistory(history);

						showHistoryList(view, history);
					}
				}

				@Override
				public void onFailure(Call<ResponseBody> call, Throwable throwable)
				{

				}
			});
		}
		else showHistoryList(view, history.getList());
	}

	public void showHistoryList(View view, ArrayList<Service> data)
	{

		for (Service s : data)
			Log.d("SERVICETEST", s.toString());

		RecyclerView list = view.findViewById(R.id.list);
		list.setLayoutManager(new LinearLayoutManager(getContext()));
		list.setAdapter(new HistoryListAdapter(getContext(), data));
	}
}
