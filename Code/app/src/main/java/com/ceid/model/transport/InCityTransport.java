package com.ceid.model.transport;
import com.ceid.util.DateFormat;

public abstract class InCityTransport extends Transport
{
    //private String emmision;

    public InCityTransport(String license_plate, String model, String manufacturer, DateFormat manuf_date)
    {
        super(license_plate, model, manufacturer, manuf_date);
    }
}
