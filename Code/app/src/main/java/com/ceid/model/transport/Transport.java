package com.ceid.model.transport;

import com.ceid.util.*;

public abstract class Transport
{
    private int id;
    private String model;
    private String manufacturer;
    private String manuf_year;
    //field for Tracker
    //private PositiveInteger seat_capacity;

    public Transport(int id, String model, String manufacturer, String manuf_year)
    {
        this.id = id;
        this.model = model;
        this.manufacturer = manufacturer;
        this.manuf_year = manuf_year;
    }

    public String getModel() {
        return model;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getManufYear() {
        return manuf_year;
    }

    public int getId() {
        return id;
    }
}
