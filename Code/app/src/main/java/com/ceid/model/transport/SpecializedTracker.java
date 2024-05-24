package com.ceid.model.transport;

import com.ceid.util.Coordinates;
import com.ceid.util.PositiveInteger;

public class SpecializedTracker extends VehicleTracker
{
	PositiveInteger gas;

	public SpecializedTracker(Coordinates coords, PositiveInteger gas)
	{
		super(coords);

		this.gas = gas;
	}

	public SpecializedTracker(Coordinates coords,double distance,boolean stopped ,PositiveInteger gas)
	{
		super(coords,distance,stopped);

		this.gas = gas;
	}

	public PositiveInteger getGas()
	{
		return gas;
	}
}
