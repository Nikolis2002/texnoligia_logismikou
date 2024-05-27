package com.ceid.model.service;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.Serializable;

public class Rating implements Serializable
{
	private Integer vehicleStars = null;
	private Integer driverStars = null;
	private Integer garageStars = null;
	private String comment;
	private RatingType type;

	public Rating(String comment, int vehicleStars)
	{
		this.comment = comment;
		this.vehicleStars = vehicleStars;

		this.type = RatingType.RENTAL;
	}

	public Rating(String comment, int vehicleStars, int otherStars, RatingType type)
	{
		this.comment = comment;
		this.vehicleStars = vehicleStars;

		this.type = type;

		switch(type)
		{
			case TAXI: this.driverStars = otherStars; break;
			case OUTCITY: this.garageStars = otherStars; break;
		}
	}

	public static Rating makeRating(JsonNode rating)
	{
		if (rating == null)
			return null;
		else
			return new Rating(rating);
	}

	public Rating(JsonNode rating)
	{
		this.vehicleStars = rating.get("vehicle_stars").asInt();
		this.garageStars = rating.path("garage_stars").asInt(-1);
		this.driverStars = rating.path("driver_stars").asInt(-1);
		this.comment = rating.get("comment").asText();

		if ((this.garageStars == -1) && (this.vehicleStars == -1))
		{
			this.garageStars = null;
			this.vehicleStars = null;
			this.type = RatingType.RENTAL;
		}
		else if (this.garageStars == -1)
		{
			this.garageStars = null;
			this.type = RatingType.TAXI;
		}
		else
		{
			this.driverStars = null;
			this.type = RatingType.OUTCITY;
		}
	}

	public String getComment()
	{
		return comment;
	}

	public int getVehicleStars()
	{
		return vehicleStars;
	}

	public int getGarageStars()
	{
		return garageStars;
	}

	public int getDriverStars()
	{
		return driverStars;
	}

	public RatingType getType()
	{
		return type;
	}
}
