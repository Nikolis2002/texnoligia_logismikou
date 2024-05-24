package com.ceid.model.transport;

public class Van extends OutCityTransport {

    public Van(String license_plate, double rate, int seats, int id, String model, String manufacturer, String manuf_year)
    {
        super(license_plate, rate, seats, id, model, manufacturer, manuf_year);
    }
}
