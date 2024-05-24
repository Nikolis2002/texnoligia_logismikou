package com.ceid.model.transport;

import androidx.annotation.NonNull;

import com.ceid.model.payment_methods.CurrencyType;
import com.ceid.util.Coordinates;
import com.ceid.util.DateFormat;

import java.util.ArrayList;

public abstract class Rental extends InCityTransport
{
    private double rate;
    private VehicleTracker tracker;
    private boolean freeStatus;

    public Rental(boolean freeStatus, int id, String model, String manufacturer, String manuf_year, double rate, Coordinates coords)
    {
        super(id, model, manufacturer, manuf_year);

        this.rate = rate;
        this.freeStatus = freeStatus;

        this.tracker = new VehicleTracker(coords);
    }

    public Rental()
    {
        super();

        this.rate = 0;
        this.freeStatus = false;
        this.tracker = null;
    }

    public Rental(boolean freeStatus, int id, String model, String manufacturer, String manuf_year, double rate, VehicleTracker tracker)
    {
        super(id, model, manufacturer, manuf_year);

        this.rate = rate;
        this.freeStatus = freeStatus;

        this.tracker = tracker;
    }

    //public updatePosition(Coordinates coords)

    public double getRate()
    {
        return rate;
    }

    public boolean isFree()
    {
        return freeStatus;
    }

    public void setFreeStatus(boolean freeStatus)
    {
        this.freeStatus = freeStatus;
    }

    public VehicleTracker getTracker()
    {
        return tracker;
    }

    public abstract boolean requiresLicense();
    public abstract boolean validLicense(String license);

    @NonNull
    public abstract String toString();
}
