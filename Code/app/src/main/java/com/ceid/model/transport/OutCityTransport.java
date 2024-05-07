package com.ceid.model.transport;

import com.ceid.model.payment_methods.CurrencyType;
import com.ceid.util.DateFormat;
import com.ceid.util.PositiveInteger;

public abstract class OutCityTransport extends Transport{

    private String licence_plate;

    public OutCityTransport(String licence_plate, int id, String model, String manufacturer, DateFormat manuf_date) {
        super(id, model, manufacturer, manuf_date);
        this.licence_plate = licence_plate;
    }
}
