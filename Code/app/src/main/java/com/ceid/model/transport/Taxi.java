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

    public Taxi(int id, String model, String manufacturer, String manuf_year,String licence_plate,Coordinates coords)
    {
        super(id, model, manufacturer, manuf_year);
        this.licence_plate=licence_plate;
        this.coords=coords;
    }

    public Taxi(int id, String model, String manufacturer, String manuf_year,String licence_plate)
    {
        super(id, model, manufacturer, manuf_year);
        this.licence_plate=licence_plate;
        this.coords=null;
    }


    public String getLicence_plate() {
        return licence_plate;
    }

    public void setLicence_plate(String licence_plate) {
        this.licence_plate = licence_plate;
    }

    public Coordinates getCoords() {
        return coords;
    }

    public void setCoords(Coordinates coords) {
        this.coords = coords;
    }
}
