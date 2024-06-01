package com.ceid.model.service;

import androidx.annotation.NonNull;

import com.ceid.util.DateFormat;
import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDateTime;

public class Coupon
{
	private int id;
	private String name;
	private int points;
	private double money;
	private LocalDateTime expirationDate;
	private int supply;

	//Constructors
	//=======================================================================================

	public Coupon(int id, String name, int points, double money, LocalDateTime expirationDate, int supply)
	{
		this.id = id;
		this.name = name;
		this.points = points;
		this.money = money;
		this.expirationDate = expirationDate;
		this.supply = supply;
	}

	public Coupon(JsonNode offerData)
	{
		this.id = offerData.get("id").asInt();
		this.name = offerData.get("name").asText();
		this.points = offerData.get("points").asInt();
		this.money = offerData.get("value").asDouble();
		this.expirationDate = DateFormat.parseFromJS(offerData.get("expiration_date").asText());
		this.supply = offerData.get("supply").asInt();
	}

	//Getters
	//=======================================================================================

	public int getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	public int getPoints()
	{
		return points;
	}

	public double getMoney()
	{
		return money;
	}

	public LocalDateTime getExpirationDate()
	{
		return expirationDate;
	}

	public int getSupply()
	{
		return supply;
	}

	//Extra
	//=======================================================================================

	@NonNull
	public String toString()
	{
		return String.format("\n=====================================================\nID: %d\nName: %s\nPoints: %d\nMoney: %.02f€\nExpiration Date: %s\nSupply: %d\n=====================================================", id, name, points, money, DateFormat.format(expirationDate), supply);
	}

	public void updateSupply(int supply)
	{
		this.supply = supply;
	}

	public void updateSupply()
	{
		this.supply--;
	}

	public boolean limited()
	{
		return this.supply != -1;
	}

	public boolean hasExpired()
	{
		return expirationDate.isBefore(LocalDateTime.now());
	}
}

