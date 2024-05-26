package com.ceid.model.service;

import com.ceid.util.Coordinates;

public class GasStation
{
	private int id;
	private Coordinates coords;
	private double gasPrice;

	public GasStation(int id, Coordinates coords, double gasPrice)
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

	public double getGasPrice()
	{
		return gasPrice;
	}

	public double calculateGasPrice(int gas)
	{
		return gasPrice*gas;
	}

}
