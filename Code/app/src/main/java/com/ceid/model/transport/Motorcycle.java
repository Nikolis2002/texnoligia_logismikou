package com.ceid.model.transport;
import com.ceid.model.payment_methods.Currency;
import com.ceid.model.payment_methods.CurrencyType;
import com.ceid.util.Coordinates;
import com.ceid.util.DateFormat;
import com.ceid.util.Fuel;
import com.ceid.util.PositiveInteger;

import java.util.ArrayList;


public class Motorcycle extends Rental implements Fuel {

	//?
	private PositiveInteger gas;
	private String license_plate;

	public Motorcycle(String license_plate, boolean freeStatus, int id, String model, String manufacturer, String manuf_year, Currency rate, Coordinates coords, PositiveInteger gas) {
		super(freeStatus, id, model, manufacturer, manuf_year, rate, new SpecializedTracker(coords, gas));

		this.license_plate = license_plate;
	}

	public Motorcycle()
	{
		super();
		this.license_plate = null;
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

	public String getLicensePlate()
	{
		return license_plate;
	}
}
