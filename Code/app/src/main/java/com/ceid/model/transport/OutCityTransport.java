package com.ceid.model.transport;

import com.ceid.model.payment_methods.CurrencyType;
import com.ceid.util.DateFormat;
import com.ceid.util.PositiveInteger;

public class OutCityTransport extends Transport{

    public OutCityTransport(String licence_plate, String model, String manufacturer, DateFormat manuf_date) {
        super(licence_plate, model, manufacturer, manuf_date);
    }
}
