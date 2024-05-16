package com.ceid.model.service;

import com.ceid.model.payment_methods.Payment;
import com.ceid.model.transport.Transport;

import java.time.LocalDateTime;

public class Service
{
    private int id;
    private LocalDateTime creationDate;
    private Payment payment;
    private Rating rating;
    private Transport transport;

    public Service(int id, LocalDateTime creationDate, Payment payment, Rating rating, Transport transport) {
        this.id = id;
        this.creationDate = creationDate;
        this.payment = payment;
        this.rating = rating;
        this.transport = transport;
    }

}
