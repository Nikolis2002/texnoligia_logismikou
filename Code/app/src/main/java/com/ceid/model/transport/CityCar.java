package com.ceid.model.transport;
import com.ceid.model.payment_methods.Currency;
import com.ceid.model.payment_methods.CurrencyType;
import com.ceid.util.Coordinates;
import com.ceid.util.DateFormat;
import com.ceid.util.Fuel;
import com.ceid.util.PositiveInteger;

import java.util.ArrayList;


public class CityCar extends Rental implements Fuel {

    //?
    private PositiveInteger gas;

    public CityCar(String license_plate, String model, String manufacturer, DateFormat manuf_date, ArrayList<String> accessibilityFeatures, Currency rate, Coordinates coords) {
        super(license_plate, model, manufacturer, manuf_date, accessibilityFeatures, rate, coords);
    }

    //rest of  the car todo!

    @Override
    public PositiveInteger getFuel() {
        // TODO

        return this.gas;
    }

    @Override
    public void setFuel(int value) {
        // TODO
    }

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
}
