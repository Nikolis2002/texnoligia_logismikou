package com.ceid.model.service;

import com.ceid.model.payment_methods.Payment;
import com.ceid.model.transport.Transport;

import java.time.LocalDateTime;

public class TaxiService extends Service{

    public TaxiService(int id, LocalDateTime creationDate, Payment payment, Transport transport) {
        super(id, creationDate, payment, transport);
    }

}
