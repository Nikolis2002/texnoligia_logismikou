package com.ceid.model.transport;

import com.ceid.model.payment_methods.Currency;
import com.ceid.util.Coordinates;

import java.util.Timer;

public class VehicleTracker extends Tracker
{
	private double distanceTraveled;
	private int duration; //in seconds

	//private Timer timer;

	public VehicleTracker(Coordinates coords, int duration, double distanceTraveled)
	{
		super(coords);

		this.duration = duration;
		this.distanceTraveled = distanceTraveled;
	}

	public VehicleTracker(Coordinates coords)
	{
		super(coords);

		this.duration = 0;
		this.distanceTraveled = 0;
	}

	public boolean checkStoppedVehicle()
	{
		return true;
	}

	public int getDuration()
	{
		return duration;
	}

	public double getDistanceTraveled()
	{
		return distanceTraveled;
	}

	//public void startTimer()
	//public Currency calculateCharge()
	//public int calculatePoints()
}
