package com.ceid.model.transport;
import com.ceid.model.payment_methods.Currency;
import com.ceid.model.payment_methods.CurrencyType;
import com.ceid.util.Coordinates;
import com.ceid.util.DateFormat;
import com.ceid.util.Fuel;
import com.ceid.util.PositiveInteger;

import java.util.ArrayList;


public class CityCar extends Rental {

    //?
    //private PositiveInteger gas;
    private String licence_plate;

    public CityCar(String license_plate, boolean freeStatus, int id, String model, String manufacturer, String manuf_year, ArrayList<String> accessibilityFeatures, Currency rate, Coordinates coords, PositiveInteger gas) {
        super(freeStatus, id, model, manufacturer, manuf_year, accessibilityFeatures, rate, new SpecializedTracker(coords, gas));
        this.licence_plate = license_plate;
    }

    //rest of  the car todo!

    @Override
    public boolean requiresLicense()
    {
        return true;
    }

    @Override
    public boolean validLicense(String license)
    {
        //TODO
        return true;
    }

    public String getLicencePlate()
    {
        return licence_plate;
    }
}
