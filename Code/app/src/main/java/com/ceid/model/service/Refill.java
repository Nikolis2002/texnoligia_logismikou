package com.ceid.model.service;

import com.ceid.model.transport.SpecializedTracker;
import com.ceid.util.PositiveInteger;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Refill implements Serializable
{

	private LocalDateTime date;
	private GasStation gasStation;
	private PositiveInteger startGas;
	private PositiveInteger endGas;
	private boolean completed = false; //Refill may fail due to post-refill tracker failure

	public Refill(LocalDateTime date, GasStation gasStation, PositiveInteger startGas, PositiveInteger endGas)
	{
		this.date = date;
		this.gasStation = gasStation;
		this.startGas = startGas;
		this.endGas = endGas;
	}

	public LocalDateTime getDate()
	{
		return date;
	}

	public GasStation getGasStation()
	{
		return gasStation;
	}

	public PositiveInteger getStartGas()
	{
		return startGas;
	}

	public PositiveInteger getEndGas()
	{
		return endGas;
	}

	public boolean isCompleted()
	{
		return completed;
	}

	public void setCompleted(boolean completed)
	{
		this.completed = completed;
	}

	public void calculatePoints(Service service)
	{
		int points = 0;
		service.addPoints(points);
	}
}
