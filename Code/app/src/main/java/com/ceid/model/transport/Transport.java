package com.ceid.model.transport;

import com.ceid.util.*;

public abstract class Transport
{
    private int id;
    private String model;
    private String manufacturer;
    private DateFormat manuf_date;
    //field for Tracker
    //private PositiveInteger seat_capacity;

    public Transport(int id, String model, String manufacturer, DateFormat manuf_date)
    {
        this.id = id;
        this.model = model;
        this.manufacturer = manufacturer;
        this.manuf_date = manuf_date;
    }

    public String getModel() {
        return model;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public DateFormat getManuf_date() {
        return manuf_date;
    }

    public int getId() {
        return id;
    }
}
