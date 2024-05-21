package com.ceid.model.service;

import com.ceid.model.payment_methods.Payment;
import com.ceid.model.transport.Transport;

import java.time.LocalDateTime;

public class TaxiService extends Service{


    public TaxiService(LocalDateTime creationDate, Payment payment, Rating rating, Transport transport) {
        super(creationDate, payment, rating, transport);

    }


}
