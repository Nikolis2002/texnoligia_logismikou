package com.ceid.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.ceid.Network.ApiClient;
import com.ceid.Network.ApiService;
import com.ceid.Network.jsonStringParser;
import com.ceid.model.transport.Garage;
import com.ceid.model.transport.OutCityCar;
import com.ceid.model.transport.OutCityTransport;
import com.ceid.model.transport.Van;
import com.ceid.model.users.Customer;
import com.ceid.model.users.User;
import com.ceid.util.DateFormat;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GarageReservationForm extends AppCompatActivity
{
	private OutCityTransport vehicle;
	private Garage garage;
	private Date selectedDate = null;
	private int hours = 12;
	private int minutes = 10;
	private int daysToRent = 1;

	Customer customer = (Customer) User.getCurrentUser();

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.garage_reservation_form);

		//Get selected garage and vehicle
		//==========================================================================================
		this.vehicle = (OutCityTransport) getIntent().getExtras().getSerializable("vehicle");
		this.garage = (Garage) getIntent().getExtras().getSerializable("garage");

		assert vehicle != null;
		assert garage != null;

		//Text
		//==========================================================================================
		TextView title = findViewById(R.id.textTitle);
		TextView rate = findViewById(R.id.textRate);
		TextView seats = findViewById(R.id.textSeats);
		TextView plate = findViewById(R.id.textPlate);

		title.setText(String.format("%s %s (%s)", vehicle.getManufacturer(), vehicle.getModel(), vehicle.getManufYear()));
		rate.setText(String.format("%.02f€/day", vehicle.getRate()));
		seats.setText(String.format("%d", vehicle.getSeats()));
		plate.setText(String.format("%s", vehicle.getLicensePlate()));

		//Icon
		//==========================================================================================
		ImageView img = findViewById(R.id.imageView);

		if (vehicle instanceof OutCityCar)
			img.setImageResource(R.drawable.out_city_car);
		else if (vehicle instanceof Van)
			img.setImageResource(R.drawable.out_city_van);

		//Garage info
		//==========================================================================================
		TextView name = findViewById(R.id.nameField);
		TextView address = findViewById(R.id.addressField);
		TextView hours = findViewById(R.id.hoursField);

		name.setText(garage.getName());
		address.setText(garage.getAddress());
		hours.setText(garage.getAvailableHours());
	}

	//Input date and time
	public void onDatetimeClick(View view)
	{
		MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
				.setTitleText("Select date")
				.setSelection(GarageReservationForm.this.selectedDate != null ? GarageReservationForm.this.selectedDate.getTime() : null)
				.build();

		datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>(){

			@Override
			public void onPositiveButtonClick(Long dateSelection)
			{
				MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
						.setTimeFormat(TimeFormat.CLOCK_12H)
						.setHour(GarageReservationForm.this.hours)
						.setMinute(GarageReservationForm.this.minutes)
						.setTitleText("Select time")
						.build();

				timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View view)
					{
						GarageReservationForm.this.selectedDate = new Date(dateSelection);
						SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

						String txt = String.format("%s, %02d:%02d", dateFormat.format(selectedDate),timePicker.getHour(), timePicker.getMinute());

						TextInputEditText textInput = (TextInputEditText) findViewById(R.id.datetimeText);
						textInput.setText(txt);

						GarageReservationForm.this.hours = timePicker.getHour();
						GarageReservationForm.this.minutes = timePicker.getMinute();
					}
				});

				timePicker.show(getSupportFragmentManager(), "tag");
			}
		});

		datePicker.show(getSupportFragmentManager(), "tag");
	}

	//Back button
	public void onCancel(View view)
	{
		finish();
	}

	//Continue button
	public void onSubmit(View view)
	{
		//Empty field checks
		//=======================================================================================
		String daysText = ((TextView)findViewById(R.id.daysText)).getText().toString();

		if (selectedDate == null)
		{
			invalidFormMsg("Empty Datetime", "Please insert the date you want to pickup the vehicle on");
			return;
		}

		if (daysText.isEmpty())
		{
			invalidFormMsg("Empty Field", "Please insert how many days you want to rent the vehicle for");
			return;
		}

		daysToRent = Integer.parseInt(daysText);

		//Pattern to extract day and time from the garage info string
		//=======================================================================================
		Pattern pattern = Pattern.compile("(?<d1>[A-Z][a-z]{2})-(?<d2>[A-Z][a-z]{2})\\s(?<t1>\\d{2}:\\d{2})-(?<t2>\\d{2}:\\d{2})");
		Matcher matcher = pattern.matcher(garage.getAvailableHours());

		//Check if day is within working hours
		//=======================================================================================
		int dayValue = DateFormat.dayOfWeekNum(selectedDate);

		if (matcher.find())
		{
			if (!(dayValue >= DateFormat.dayOfWeekNum(matcher.group("d1")) && dayValue <= DateFormat.dayOfWeekNum(matcher.group("d2"))))
			{
				invalidFormMsg("Day Error", "Date is not within working hours");
				return;
			}

			//Check if time is within working hours
			//=======================================================================================
			DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

			LocalTime startTime = LocalTime.parse(matcher.group("t1"), timeFormatter);
			LocalTime endTime = LocalTime.parse(matcher.group("t2"), timeFormatter);
			LocalTime checkTime = LocalTime.parse(String.format("%02d:%02d", this.hours, this.minutes), timeFormatter);

			if (checkTime.isBefore(startTime) || checkTime.isAfter(endTime))
			{
				invalidFormMsg("Time Error", "Time is not within working hours");
				return;
			}

			//Check if datetime is within the current week
			//=======================================================================================
			LocalDateTime currentDatetime = LocalDateTime.now();
			LocalDateTime selectedDatetime = LocalDateTime.of(DateFormat.toLocalDate(selectedDate), checkTime);

			long daysBetween = ChronoUnit.DAYS.between(currentDatetime, selectedDatetime);

			if (selectedDatetime.isBefore(currentDatetime))
			{
				invalidFormMsg("Achievement Unlocked: Time Traveler", "This time is in the past");
				return;
			}

			if (daysBetween >= 7)
			{
				invalidFormMsg("Not within current week", "The selected day must be within the next 7 days");
				return;
			}

			//Form was valid. Check wallet
			//=======================================================================================
			double balance = customer.getWallet().getBalance();

			if (balance < daysToRent*vehicle.getRate())
			{
				noMoneyMsg();
				return;
			}

			//Withdraw from wallet
			customer.getWallet().withdraw(daysToRent*vehicle.getRate());

			//If all went well, save to database
			//=======================================================================================

			List<Map<String,Object>> values = new ArrayList<>();
			Map<String, Object> insert= new LinkedHashMap<>();
			insert.put("name",customer.getUsername());
			insert.put("value",daysToRent*vehicle.getRate());
			insert.put("method","WALLET");
			insert.put("creationDate",LocalDateTime.now());
			insert.put("status","ONGOING");
			insert.put("status_date",null);
			insert.put("earned_points",0);
			insert.put("car_id",vehicle.getId());
			insert.put("num_days",daysToRent);
			values.add(insert);

			ApiService api= ApiClient.getApiService();
			String parser= jsonStringParser.createJsonString("insertOutCityService",values);
			Call<ResponseBody> call= api.callProcedure(parser);

			call.enqueue(new Callback<ResponseBody>() {
				@Override
				public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
					if (response.isSuccessful()) {

						View popupView = LayoutInflater.from(GarageReservationForm.this).inflate(R.layout.garage_popup, null);

						// Create the popup window
						PopupWindow popupWindow = new PopupWindow(
								popupView,
								ViewGroup.LayoutParams.MATCH_PARENT,
								ViewGroup.LayoutParams.MATCH_PARENT
						);

						String yourDate = DateFormat.humanReadable(selectedDatetime);
						TextView textView=popupView.findViewById(R.id.textPop);
						textView.setText(yourDate);

						// Show the popup window
						popupWindow.showAtLocation(
								findViewById(android.R.id.content),
								Gravity.CENTER,
								0,
								0
						);

					} else {
						Log.d("Response", "Unsuccessful");
					}
				}

				@Override
				public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
					Log.e("Error", "Failed to fetch data: " + t.getMessage());
				}
			});
		}
	}

	public void onClose(View view){
		Intent intent=new Intent(getApplicationContext(),MainScreen.class);
		startActivity(intent);
	}

	//ERRORS
	//========================================================================================

	public void invalidFormMsg(String formErrorTitle, String formErrorMsg)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle(formErrorTitle);
		builder.setMessage(formErrorMsg);
		builder.setCancelable(false);

		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});

		AlertDialog alert = builder.create();
		alert.setCanceledOnTouchOutside(false);
		alert.show();
	}

	public void noMoneyMsg()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle("Insufficient funds");
		builder.setMessage(String.format("You do not have enough money in your wallet for the rental. Current balance: %.02f€", customer.getWallet().getBalance()));

		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});

		AlertDialog alert = builder.create();
		alert.setCanceledOnTouchOutside(false);
		alert.show();
	}
}
