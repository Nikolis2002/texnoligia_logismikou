package com.ceid.model.service;

import com.ceid.model.payment_methods.Payment;
import com.ceid.model.transport.Transport;
import com.ceid.util.PositiveInteger;

import java.time.LocalDateTime;

public class OutCityService extends Service{

	//I DO NOT WANT THE ENTIRE GARAGE
	//I AM NOT INTERESTED IN THE CONTAINED VEHICLES
	//ALL I WANT IS THE GARAGE NAME AND ID
	private String garageName;
	private int garageId;

	public OutCityService(String garageName, int garageId, int id, LocalDateTime creationDate, Payment payment, Rating rating, Transport transport) {
		super(id, creationDate, payment, rating, transport);

		this.garageName = garageName;
		this.garageId = garageId;
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
