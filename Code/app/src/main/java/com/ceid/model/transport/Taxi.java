package com.ceid.model.transport;

import com.ceid.util.Coordinates;

public class Taxi extends InCityTransport {
    private String licence_plate;
    private Coordinates coords;

    public Taxi(int id, String model, String manufacturer, String manuf_year,String licence_plate,double lat,double lng)
    {
        super(id, model, manufacturer, manuf_year);
        this.licence_plate=licence_plate;
        this.coords=new Coordinates(lat,lng);
    }
}
