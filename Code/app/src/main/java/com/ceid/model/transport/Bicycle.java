package com.ceid.model.transport;
import com.ceid.model.payment_methods.Currency;
import com.ceid.model.payment_methods.CurrencyType;
import com.ceid.util.Coordinates;
import com.ceid.util.DateFormat;
import com.ceid.util.Fuel;
import com.ceid.util.PositiveInteger;

import java.util.ArrayList;


public class Bicycle extends Rental {

	public Bicycle(boolean freeStatus, int id, String model, String manufacturer, String manuf_year, ArrayList<String> accessibilityFeatures, Currency rate, Coordinates coords) {
		super(freeStatus, id, model, manufacturer, manuf_year, accessibilityFeatures, rate, coords);
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
}
