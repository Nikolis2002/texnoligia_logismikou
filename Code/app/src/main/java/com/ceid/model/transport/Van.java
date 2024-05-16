package com.ceid.model.transport;

import com.ceid.model.payment_methods.Currency;

public class Van extends OutCityTransport {

    public Van(String license_plate, Currency rate, int seats, int id, String model, String manufacturer, String manuf_year)
    {
        super(license_plate, rate, seats, id, model, manufacturer, manuf_year);
    }
}
