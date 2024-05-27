package com.ceid.model.service;

import com.ceid.model.payment_methods.Payment;
import com.ceid.model.transport.Bicycle;
import com.ceid.model.transport.CityCar;
import com.ceid.model.transport.ElectricScooter;
import com.ceid.model.transport.Motorcycle;
import com.ceid.model.transport.OutCityCar;
import com.ceid.model.transport.OutCityTransport;
import com.ceid.model.transport.Rental;
import com.ceid.model.transport.Transport;
import com.ceid.model.transport.Van;
import com.ceid.util.DateFormat;
import com.ceid.util.PositiveInteger;
import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDateTime;
import java.util.Objects;

public class OutCityService extends Service{

	//I DO NOT WANT THE ENTIRE GARAGE
	//I AM NOT INTERESTED IN THE CONTAINED VEHICLES
	//ALL I WANT IS THE GARAGE NAME AND ID
	private String garageName;
	private int garageId;

	public OutCityService(String garageName, int garageId, int id, LocalDateTime creationDate, Payment payment, Rating rating, int earnedPoints, Transport transport) {
		super(id, creationDate, payment, rating, earnedPoints, transport);

		this.garageName = garageName;
		this.garageId = garageId;
	}

	public static OutCityTransport makeVehicle(JsonNode vehicleData)
	{
		OutCityTransport oct = null;
		String vehicleType = vehicleData.get("type").asText();

		if (Objects.equals(vehicleType, "van"))
			oct = new Van(vehicleData);
		else if (Objects.equals(vehicleType, "car"))
			oct = new OutCityCar(vehicleData);

		return oct;
	}

	public OutCityService(JsonNode data)
	{
		super(
				data.get("id").asInt(),
				DateFormat.parseFromJS(data.get("creation_date").asText()),
				new Payment(
						data.get("amount").asDouble(),
						data.get("payment_method").asText()
				),
				Rating.makeRating(data.get("rating")),
				data.get("earned_points").asInt(),
				makeVehicle(data.get("vehicle"))
		);

		this.garageId = data.get("vehicle").get("garage_id").asInt();
		this.garageName = data.get("vehicle").get("garage_id").asText();
	}

	public String getGarageName()
	{
		return garageName;
	}

	public int getGarageId()
	{
		return garageId;
	}
}
