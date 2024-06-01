package com.ceid.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ceid.Network.ApiClient;
import com.ceid.Network.ApiService;
import com.ceid.Network.jsonStringParser;
import com.ceid.model.service.Coupon;
import com.ceid.model.users.Customer;
import com.ceid.model.users.User;
import com.ceid.util.GenericCallback;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OfferScreen extends AppCompatActivity implements View.OnClickListener
{
	Customer customer;
	TextView balanceField, pointsField;
	OfferListAdapter adapter;
	ArrayList<Coupon> data;

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.offer_screen);

		customer = (Customer) User.getCurrentUser();

		//Initialize fields
		//========================================================================================
		balanceField = findViewById(R.id.balanceText);
		pointsField = findViewById(R.id.pointsText);

		updateFields();


		//Get offers from database
		//========================================================================================

		ApiService api = ApiClient.getApiService();

		Call<ResponseBody> call = api.getTableData("valid_coupons");

		call.enqueue(new Callback<ResponseBody>() {

			@Override
			public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
			{
				ObjectMapper mapper = new ObjectMapper();
				JsonNode jsonData;

				try
				{
					jsonData = mapper.readTree(response.body().string());
				}
				catch (IOException e)
				{
					throw new RuntimeException(e);
				}

				data = new ArrayList<>();

				for (JsonNode node : jsonData)
					data.add(new Coupon(node));

				//Check if coupons were retrieved
				//=================================================================================================
				if (data.isEmpty())
				{
					noOffersMsg();
				}
				else
				{
					//Show list
					//========================================================================================

					RecyclerView list = findViewById(R.id.list);
					list.setLayoutManager(new LinearLayoutManager(OfferScreen.this));

					adapter = new OfferListAdapter(OfferScreen.this, data, OfferScreen.this);

					list.setAdapter(adapter);
				}
			}

			@Override
			public void onFailure(Call<ResponseBody> call, Throwable throwable)
			{

			}
		});
	}

	public void updateFields()
	{
		balanceField.setText(String.format("Balance: %.02f€", customer.getWallet().getBalance()));
		pointsField.setText(String.format("Points: %d", customer.getPoints().getPoints()));
	}


	//Clicking on a coupon
	//=============================================================================================
	@Override
	public void onClick(View view)
	{
		int pos = (int)view.getTag();
		Coupon selectedCoupon = adapter.getItem(pos);
		Log.d("COUPONTEST", selectedCoupon.toString());


		//Confirmation message
		//=======================================================================================
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle("Proceed?");
		builder.setMessage(String.format("Are you sure you want to redeem the coupon \"%s\"?",selectedCoupon.getName()));
		builder.setCancelable(false);

		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();

				//Check if customer has enough points
				int customerPoints = customer.getPoints().getPoints();

				if (customerPoints < selectedCoupon.getPoints())
				{
					insufficientPointsMsg();
				}
				else
				{
					//Check if coupon has expired

					if (selectedCoupon.hasExpired())
					{
						expiredMsg();
					}
					else
					{
						if (selectedCoupon.limited())
						{
							//Check coupon status on the database
							//===================================================================================================
							List<Map<String,Object>> values = new ArrayList<>();
							java.util.Map<String, Object> params = new LinkedHashMap<>();

							params.put("coupon_id", selectedCoupon.getId());
							values.add(params);

							String jsonString = jsonStringParser.createJsonString("check_coupon", values);

							ApiService api = ApiClient.getApiService();
							Call<ResponseBody> call = api.callProcedure(jsonString);

							call.enqueue(new Callback<ResponseBody>(){
								@Override
								public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
								{
									ObjectMapper mapper = new ObjectMapper();
									JsonNode jsonData;

									try
									{
										jsonData = mapper.readTree(response.body().string());
									}
									catch (IOException e)
									{
										throw new RuntimeException(e);
									}

									int supply = jsonData.get(0).get(0).get("result").asInt();

									//Still in stock
									if (supply > 0)
									{
										//-1 for the coupon i am about to redeem

										selectedCoupon.updateSupply(supply-1);

										if (supply - 1 == 0)
											adapter.remove(pos);

										adapter.notifyDataSetChanged();

										redeem(selectedCoupon);
									}
									else //No longer in stock
									{
										outOfStockMsg();

										adapter.remove(pos);
										adapter.notifyDataSetChanged();
									}
								}

								@Override
								public void onFailure(Call<ResponseBody> call, Throwable throwable)
								{

								}
							});
						}
						else
						{
							redeem(selectedCoupon);
						}
					}
				}
			}
		});

		builder.setNegativeButton("No", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});

		AlertDialog confirmationMsg = builder.create();
		confirmationMsg.setCanceledOnTouchOutside(false);
		confirmationMsg.show();

	}

	private void redeem(Coupon selectedCoupon)
	{
		//Database call
		//===================================================================================================
		List<Map<String,Object>> values = new ArrayList<>();
		java.util.Map<String, Object> params = new LinkedHashMap<>();

		params.put("coupon_id", selectedCoupon.getId());
		params.put("username", customer.getUsername());
		values.add(params);

		String jsonString = jsonStringParser.createJsonString("redeem_coupon", values);

		ApiService api = ApiClient.getApiService();
		Call<ResponseBody> call = api.callProcedure(jsonString);

		call.enqueue(new Callback<ResponseBody>(){
			@Override
			public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
			{
				customer.subtractPoints(selectedCoupon.getPoints());
				customer.getWallet().addToWallet(selectedCoupon.getMoney());
				updateFields();
			}

			@Override
			public void onFailure(Call<ResponseBody> call, Throwable throwable)
			{

			}
		});
	}

	//ERRORS
	//========================================================================================

	public void noOffersMsg()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle("No offers");
		builder.setMessage("There are currently no active offers");
		builder.setCancelable(false);

		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
				finish();
			}
		});

		AlertDialog alert = builder.create();
		alert.setCanceledOnTouchOutside(false);
		alert.show();
	}

	public void insufficientPointsMsg()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle("Not enough points");
		builder.setMessage("You do not have enough points for this offer");
		builder.setCancelable(false);

		builder.setPositiveButton("Ok", (dialog, which) -> dialog.dismiss());

		AlertDialog alert = builder.create();
		alert.setCanceledOnTouchOutside(false);
		alert.show();
	}

	public void expiredMsg()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle("Coupon expired");
		builder.setMessage("The selected offer is no longer active");
		builder.setCancelable(false);

		builder.setPositiveButton("Ok", (dialog, which) -> dialog.dismiss());

		AlertDialog alert = builder.create();
		alert.setCanceledOnTouchOutside(false);
		alert.show();
	}

	public void outOfStockMsg()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle("Out of stock");
		builder.setMessage("This offer is no longer in stock");
		builder.setCancelable(false);

		builder.setPositiveButton("Ok", (dialog, which) -> dialog.dismiss());

		AlertDialog alert = builder.create();
		alert.setCanceledOnTouchOutside(false);
		alert.show();
	}
}
