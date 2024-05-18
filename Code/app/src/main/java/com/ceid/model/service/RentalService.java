package com.ceid.model.service;

import com.ceid.model.payment_methods.Payment;
import com.ceid.model.transport.Transport;

import java.time.LocalDateTime;

public class RentalService extends Service{

	private Refill refill = null;

	public RentalService(int id, LocalDateTime creationDate, Payment payment, Rating rating, Transport transport, TaxiRequest request) {
		super(id, creationDate, payment, rating, transport);
	}

	public void setRefill(Refill refill)
	{
		this.refill = refill;
	}
}
