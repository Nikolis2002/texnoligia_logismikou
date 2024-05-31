package com.ceid.model.transport;

import com.ceid.util.Coordinates;

import java.util.Timer;

public class VehicleTracker extends Tracker
{
	private double distanceTraveled;
	private boolean isStopped;

	//private Timer timer;

	public VehicleTracker(Coordinates coords, double distanceTraveled)
	{
		super(coords);

		this.distanceTraveled = distanceTraveled;
	}

	public VehicleTracker(Coordinates coords)
	{
		super(coords);

		this.distanceTraveled = 0;
	}

	public VehicleTracker(Coordinates coords, double distance, boolean isStopped) {
		super(coords);
		this.distanceTraveled=distance;
		this.isStopped=isStopped;
	}

	public double getDistanceTraveled()
	{
		return distanceTraveled;
	}

	public boolean isStopped() {
		return isStopped;
	}

	//public void startTimer()

	public int calculatePoints()
	{
		return (int) Math.floor(distanceTraveled/5);
	}
}
