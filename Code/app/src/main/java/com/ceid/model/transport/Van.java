package com.ceid.model.transport;

import com.fasterxml.jackson.databind.JsonNode;

public class Van extends OutCityTransport {

    public Van(String license_plate, double rate, int seats, int id, String model, String manufacturer, String manuf_year)
    {
        super(license_plate, rate, seats, id, model, manufacturer, manuf_year);
    }

	public Van(JsonNode vehicleData)
	{
		super(
				vehicleData.get("license_plate").asText(),
				vehicleData.get("rate").asDouble(),
				vehicleData.get("seats").asInt(),
				vehicleData.get("id").asInt(),
				vehicleData.get("model").asText(),
				vehicleData.get("manufacturer").asText(),
				vehicleData.get("manuf_year").asText()
		);
	}
}
