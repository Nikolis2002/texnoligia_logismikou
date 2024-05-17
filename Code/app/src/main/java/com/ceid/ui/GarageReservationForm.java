package com.ceid.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.ceid.model.transport.Garage;
import com.ceid.model.transport.OutCityCar;
import com.ceid.model.transport.OutCityTransport;
import com.ceid.model.transport.Van;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GarageReservationForm extends AppCompatActivity
{
	private OutCityTransport vehicle;
	private Garage garage;
	private Date selectedDate = null;
	private int hours = 12;
	private int minutes = 10;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.garage_reservation_form);

		this.vehicle = (OutCityTransport) getIntent().getExtras().getSerializable("vehicle");
		this.garage = (Garage) getIntent().getExtras().getSerializable("garage");

		assert vehicle != null;
		assert garage != null;

		//Text

		TextView title = findViewById(R.id.textTitle);
		TextView rate = findViewById(R.id.textRate);
		TextView seats = findViewById(R.id.textSeats);
		TextView plate = findViewById(R.id.textPlate);

		title.setText(String.format("%s %s (%s)", vehicle.getManufacturer(), vehicle.getModel(), vehicle.getManufYear()));
		rate.setText(String.format("%s/day", vehicle.getRate().toString()));
		seats.setText(String.format("%d", vehicle.getSeats()));
		plate.setText(String.format("%s", vehicle.getLicensePlate()));

		//Icon

		ImageView img = findViewById(R.id.imageView);

		if (vehicle instanceof OutCityCar)
			img.setImageResource(R.drawable.out_city_car);
		else if (vehicle instanceof Van)
			img.setImageResource(R.drawable.out_city_van);

		//Garage info

		TextView name = findViewById(R.id.nameField);
		TextView address = findViewById(R.id.addressField);
		TextView hours = findViewById(R.id.hoursField);

		name.setText(garage.getName());
		address.setText(garage.getAddress());
		hours.setText(garage.getAvailableHours());
	}

	//Input time

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
	public void onBack(View view)
	{
		finish();
	}

	//Continue button
	public void onContinue(View view)
	{
		//CHECK FORM VALIDITLY
		//WE NEED TO CHECK IF THE SELECTED DATE IS WITHIN THE AVAILABLE HOURS
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

		LocalDate localDate = LocalDate.parse(dateFormat.format(selectedDate), DateTimeFormatter.ofPattern("dd-MM-yyyy"));

		String day = localDate.getDayOfWeek().name().substring(0, 3);
		day = day.charAt(0) + day.substring(1).toLowerCase();

		java.util.Map<String, Integer> dayMap = new HashMap<String, Integer>();

		dayMap.put("Mon", 0);
		dayMap.put("Tue", 1);
		dayMap.put("Wed", 2);
		dayMap.put("Thu", 3);
		dayMap.put("Fri", 4);
		dayMap.put("Sat", 5);
		dayMap.put("Sun", 6);

		Log.d("REGEXTEST", garage.getAvailableHours());

		Pattern pattern = Pattern.compile("(?<d1>[A-Z][a-z]{2})-(?<d2>[A-Z][a-z]{2})\\s(?<t1>\\d{2}:\\d{2})-(?<t2>\\d{2}:\\d{2})");
		Matcher matcher = pattern.matcher(garage.getAvailableHours());

		Log.d("REGEXTEST", matcher.toString());

		int dayValue = dayMap.get(day);

		if (matcher.find())
		{
			if (!(dayValue >= dayMap.get(matcher.group("d1")) && dayValue <= dayMap.get(matcher.group("d2"))))
			{
				Log.d("DAYERROR", "Date is not within range");
			}

			//Check time

			DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

			LocalTime startTime = LocalTime.parse(matcher.group("t1"), timeFormatter);
			LocalTime endTime = LocalTime.parse(matcher.group("t2"), timeFormatter);
			LocalTime checkTime = LocalTime.parse(String.format("%02d:%02d", this.hours, this.minutes), timeFormatter);

			boolean isWithinRange = !checkTime.isBefore(startTime) && !checkTime.isAfter(endTime);

			if (checkTime.isBefore(startTime) || checkTime.isAfter(endTime))
			{
				Log.d("TIMEERROR", "Time is not within range");
			}

			//Check if it's within the current week

			LocalDateTime currentDatetime = LocalDateTime.now();

			LocalDateTime selectedDatetime = LocalDateTime.of(localDate, checkTime);

			long daysBetween = ChronoUnit.DAYS.between(currentDatetime, selectedDatetime);

			if (selectedDatetime.isBefore(currentDatetime))
			{
				Log.d("DAYSTEST", "Bro wants to time travel");
			}

			if (daysBetween >= 7)
			{
				Log.d("DAYSTEST", "Not within current week");
			}
		}
	}
}
