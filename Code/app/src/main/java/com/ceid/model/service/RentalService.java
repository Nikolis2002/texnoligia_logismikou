package com.ceid.model.service;

import com.ceid.model.payment_methods.Payment;
import com.ceid.model.transport.Transport;
import com.ceid.model.users.Points;
import com.ceid.util.PositiveInteger;

import java.time.LocalDateTime;

public class RentalService extends Service{

	private Refill refill = null;

	public RentalService(int id, LocalDateTime creationDate, Payment payment, Rating rating, int earnedPoints, Transport transport) {
		super(id, creationDate, payment, rating, earnedPoints, transport);
	}

	public void setRefill(Refill refill)
	{
		this.refill = refill;
	}
}
