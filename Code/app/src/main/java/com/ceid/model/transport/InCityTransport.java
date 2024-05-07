package com.ceid.model.transport;
import com.ceid.util.DateFormat;

public abstract class InCityTransport extends Transport
{
    //private String emmision;

    public InCityTransport(int id, String model, String manufacturer, String manuf_year)
    {
        super(id, model, manufacturer, manuf_year);
    }
}
