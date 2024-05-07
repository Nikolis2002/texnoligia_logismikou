package com.ceid.model.transport;

import com.ceid.model.payment_methods.Currency;
import com.ceid.model.payment_methods.CurrencyType;
import com.ceid.util.Coordinates;
import com.ceid.util.DateFormat;

import java.util.ArrayList;

public abstract class Rental extends InCityTransport
{
    private ArrayList<String>  accessibilityFeatures;
    private Currency rate;
    private VehicleTracker tracker;
    private boolean freeStatus;

    public Rental(boolean freeStatus, int id, String model, String manufacturer, String manuf_year, ArrayList<String> accessibilityFeatures, Currency rate, Coordinates coords)
    {
        super(id, model, manufacturer, manuf_year);

        this.accessibilityFeatures = accessibilityFeatures;
        this.rate = rate;
        this.freeStatus = freeStatus;

        this.tracker = new VehicleTracker(coords);
    }

    //public updatePosition(Coordinates coords)

    public ArrayList<String> getAccessibilityFeatures()
    {
        return accessibilityFeatures;
    }

    public void setAccessibilityFeatures(ArrayList<String> accessibilityFeatures)
    {
        this.accessibilityFeatures = accessibilityFeatures;
    }

    public void addAccessibilityFeature(String feature)
    {
        this.accessibilityFeatures.add(feature);
    }

    public Currency getRate()
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
}
