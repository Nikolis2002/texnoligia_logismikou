package com.ceid.model.transport;
import androidx.annotation.NonNull;

import com.ceid.util.Coordinates;
import com.fasterxml.jackson.databind.JsonNode;


public class ElectricScooter extends Rental {

	public ElectricScooter(boolean freeStatus, int id, String model, String manufacturer, String manuf_year, double rate, Coordinates coords) {
		super(freeStatus, id, model, manufacturer, manuf_year, rate, new VehicleTracker(coords));
	}

	public ElectricScooter()
	{
		super();
	}

	public ElectricScooter(JsonNode vehicleData)
	{
		super(
				true,
				vehicleData.get("id").asInt(),
				vehicleData.get("model").asText(),
				vehicleData.get("manufacturer").asText(),
				vehicleData.get("manuf_year").asText(),
				vehicleData.get("rate").asDouble(),
				new VehicleTracker(
						new Coordinates(vehicleData.get("coords"))
				)
		);
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
		return String.format("=======================================\nType: ElectricScooter\nID: %d\nModel: %s %s (%s)\nRate: %f\nCoords: (%f, %f)\n=======================================", this.getId(), this.getManufacturer(), this.getModel(), this.getManufYear(), this.getRate(), this.getTracker().getCoords().getLat(), this.getTracker().getCoords().getLng());
	}
}
