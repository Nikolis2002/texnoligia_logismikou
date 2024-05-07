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
    VehicleTracker tracker;
    private boolean freeStatus;

    public Rental(int id, String model, String manufacturer, DateFormat manuf_date, ArrayList<String> accessibilityFeatures, Currency rate, Coordinates coords)
    {
        super(id, model, manufacturer, manuf_date);

        this.accessibilityFeatures = accessibilityFeatures;
        this.rate = rate;

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

    public abstract boolean requiresLicense();
    public abstract boolean validLicense(String license);
}
