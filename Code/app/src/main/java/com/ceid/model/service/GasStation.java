package com.ceid.model.service;

import com.ceid.model.payment_methods.Currency;
import com.ceid.util.Coordinates;

public class GasStation
{
	private int id;
	private Coordinates coords;
	private Currency gasPrice;

	public GasStation(int id, Coordinates coords, Currency gasPrice)
	{
		this.id = id;
		this.coords = coords;
		this.gasPrice = gasPrice;
	}

	public int getid()
	{
		return id;
	}

	public Coordinates getCoords()
	{
		return coords;
	}

	public Currency getGasPrice()
	{
		return gasPrice;
	}

	public Currency calculateGasPrice(int amount)
	{
		return gasPrice.mulValue(amount);
	}
}
