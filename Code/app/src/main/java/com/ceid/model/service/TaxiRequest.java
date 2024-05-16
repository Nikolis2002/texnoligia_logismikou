package com.ceid.model.service;

import com.ceid.model.payment_methods.Payment;
import com.ceid.model.transport.Transport;
import com.ceid.model.users.TaxiDriver;
import com.ceid.util.Coordinates;

import java.time.LocalDateTime;

public class TaxiRequest extends TaxiService{

    private Coordinates pickUpLocation;
    private Coordinates destination;
    private TaxiDriver taxiDriver;
    private LocalDateTime assignmentTime;
    private LocalDateTime pickUpTime;

    public TaxiRequest(int id, LocalDateTime creationDate, Payment payment, Rating rating, Transport transport) {
        super(id, creationDate, payment, rating, transport);
    }
}
