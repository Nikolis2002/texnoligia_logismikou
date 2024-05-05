package com.ceid.model.transport;
import com.ceid.model.payment_methods.typeOfCurrency;
import com.ceid.util.DateFormat;
import com.ceid.util.Fuel;
import com.ceid.util.PositiveInteger;

import java.util.ArrayList;


public class CityCar extends CityTransport implements Fuel {
    
    private PositiveInteger gas;

    public CityCar(String id, String model, String manufacturer, DateFormat manuf_date, String licence_plate, boolean free_status, PositiveInteger seat_capacity, typeOfCurrency rental_rate, String emmision, ArrayList<String> accessibilityFeatures) {
        super(id, model, manufacturer, manuf_date, licence_plate, free_status, seat_capacity, rental_rate, emmision, accessibilityFeatures);
    }
    //rest of  the car todo!

    @Override
    public PositiveInteger getFuel() {
        // TODO
        return null;
    }

    @Override
    public void setFuel(int value) {
        // TODO
    }
}
