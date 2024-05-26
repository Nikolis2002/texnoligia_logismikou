package com.ceid.model.transport;
import androidx.annotation.NonNull;

import com.ceid.model.payment_methods.CurrencyType;
import com.ceid.util.Coordinates;
import com.ceid.util.DateFormat;
import com.ceid.util.PositiveInteger;

import java.util.ArrayList;


public class Bicycle extends Rental {

	public Bicycle(boolean freeStatus, int id, String model, String manufacturer, String manuf_year, double rate, Coordinates coords) {
		super(freeStatus, id, model, manufacturer, manuf_year, rate, new VehicleTracker(coords));
	}

	public Bicycle()
	{
		super();
	}

	@Override
	public boolean requiresLicense()
	{
		return false;
	}

	@Override
	public boolean validLicense(String license)
	{
		//License is always valid
		return true;
	}

	@NonNull
	public String toString()
	{
		return String.format("=======================================\nType: Bicycle\nID: %d\nModel: %s %s (%s)\nRate: %f\nCoords: (%f, %f)\n=======================================", this.getId(), this.getManufacturer(), this.getModel(), this.getManufYear(), this.getRate(), this.getTracker().getCoords().getLat(), this.getTracker().getCoords().getLng());
	}
}
