package com.ceid.model.transport;

import com.ceid.model.payment_methods.CurrencyType;
import com.ceid.util.DateFormat;
import com.ceid.util.PositiveInteger;

public abstract class OutCityTransport extends Transport{

    private String licence_plate;

    public OutCityTransport(String licence_plate, int id, String model, String manufacturer, String manuf_year) {
        super(id, model, manufacturer, manuf_year);
        this.licence_plate = licence_plate;
    }
}
