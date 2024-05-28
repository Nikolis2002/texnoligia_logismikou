package com.ceid.model.service;

import com.ceid.model.payment_methods.Payment;
import com.ceid.model.transport.Bicycle;
import com.ceid.model.transport.CityCar;
import com.ceid.model.transport.ElectricScooter;
import com.ceid.model.transport.Motorcycle;
import com.ceid.model.transport.Rental;
import com.ceid.model.transport.Transport;
import com.ceid.model.users.Points;
import com.ceid.util.DateFormat;
import com.ceid.util.PositiveInteger;
import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDateTime;
import java.util.Objects;

public class RentalService extends Service{

	private Refill refill = null;

	public RentalService(int id, LocalDateTime creationDate, Payment payment, Rating rating, int earnedPoints, Transport transport) {
		super(id, creationDate, payment, rating, earnedPoints, transport);
	}

	public static Rental makeRental(JsonNode vehicleData)
	{
		Rental rental = null;
		String vehicleType = vehicleData.get("type").asText();

		if (Objects.equals(vehicleType, "car"))
			rental = new CityCar(vehicleData);
		else if (Objects.equals(vehicleType, "motorcycle"))
			rental = new Motorcycle(vehicleData);
		else if (Objects.equals(vehicleType, "bicycle"))
			rental = new Bicycle(vehicleData);
		else if (Objects.equals(vehicleType, "scooter"))
			rental = new ElectricScooter(vehicleData);

		return rental;
	}

	public RentalService(JsonNode data)
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
				makeRental(data.get("vehicle"))
		);

		//I don't care about this
		this.refill = null;
	}

	public void setRefill(Refill refill)
	{
		this.refill = refill;
	}

	public Refill getRefill(){return this.refill;}
}
