package com.ceid.model.service;

public class Rating {
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
