package com.ceid.model.service;

import com.ceid.model.payment_methods.Payment;
import com.ceid.model.transport.Transport;

import java.time.LocalDateTime;

public class TaxiService extends Service{

    private TaxiServiceProgress serviceProgress;

    public TaxiService(int id, LocalDateTime creationDate, Payment payment, Rating rating, Transport transport,TaxiServiceProgress serviceProgress) {
        super(id, creationDate, payment, rating, transport);
    }
}
