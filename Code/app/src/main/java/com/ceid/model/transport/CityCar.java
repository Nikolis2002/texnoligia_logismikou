package com.ceid.model.transport;
import androidx.annotation.NonNull;

import com.ceid.util.Coordinates;
import com.ceid.util.PositiveInteger;

import java.util.ArrayList;


public class CityCar extends Rental {

    //?
    //private PositiveInteger gas;
    private String license_plate;

    public CityCar(String license_plate, boolean freeStatus, int id, String model, String manufacturer, String manuf_year, double rate, Coordinates coords, PositiveInteger gas) {
        super(freeStatus, id, model, manufacturer, manuf_year, rate, new SpecializedTracker(coords, gas));
        this.license_plate = license_plate;
    }

    public CityCar()
    {
        super();
        this.license_plate = null;
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

    public String getLicensePlate()
    {
        return license_plate;
    }

    @NonNull
    public String toString()
    {
        return String.format("=======================================\nType: CityCar\nID: %d\nLicense Plate: %s\nModel: %s %s (%s)\nRate: %f\nCoords: (%f, %f)\nGas: %d\n=======================================", this.getId(), this.license_plate, this.getManufacturer(), this.getModel(), this.getManufYear(), this.getRate(), this.getTracker().getCoords().getLat(), this.getTracker().getCoords().getLng(), ((SpecializedTracker)this.getTracker()).getGas().getValue());
    }
}
