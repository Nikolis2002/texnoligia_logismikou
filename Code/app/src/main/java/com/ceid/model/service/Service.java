package com.ceid.model.service;

import com.ceid.model.payment_methods.Payment;
import com.ceid.model.transport.Transport;

import java.time.LocalDateTime;

public abstract class Service
{
    private int id;
    private LocalDateTime creationDate;
    private Payment payment;
    private Rating rating = null;
    private Transport transport;

    public Service(int id, LocalDateTime creationDate, Payment payment, Rating rating, Transport transport) {
        this.id = id;
        this.creationDate = creationDate;
        this.payment = payment;
        this.rating = rating;
        this.transport = transport;
    }

    public Service(int id, LocalDateTime creationDate, Payment payment, Transport transport) {
        this.id = id;
        this.creationDate = creationDate;
        this.payment = payment;
        this.transport = transport;
    }

    public void rate()
    {

    }

    public Rating getRating()
    {
        return rating;
    }

}
