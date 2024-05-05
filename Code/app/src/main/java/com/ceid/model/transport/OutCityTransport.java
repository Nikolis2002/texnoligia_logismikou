package com.ceid.model.transport;

import com.ceid.model.payment_methods.typeOfCurrency;
import com.ceid.util.DateFormat;
import com.ceid.util.PositiveInteger;

public class OutCityTransport extends Transport{

    public OutCityTransport(String id, String model, String manufacturer, DateFormat manuf_date, String licence_plate, boolean free_status, PositiveInteger seat_capacity, typeOfCurrency rental_rate) {
        super(id, model, manufacturer, manuf_date, licence_plate, free_status, seat_capacity, rental_rate);
    }
}
