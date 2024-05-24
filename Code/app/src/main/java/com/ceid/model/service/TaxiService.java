package com.ceid.model.service;

import com.ceid.model.payment_methods.Payment;
import com.ceid.model.transport.Transport;

import java.time.LocalDateTime;

public class TaxiService extends Service{

    private TaxiRequest request;

    public TaxiService(int id, LocalDateTime creationDate, Payment payment, Rating rating, Transport transport, TaxiRequest request) {
        super(id, creationDate, payment, rating, transport);

        this.request = request;
    }

    public TaxiService(int id, Payment.Method payment){
        super(id,payment);
    }

    public TaxiRequest getRequest()
    {
        return request;
    }

}