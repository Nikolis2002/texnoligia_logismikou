package com.ceid.model.transport;
import com.ceid.util.Coordinates;
import com.ceid.util.Location;

import java.util.ArrayList;

public class Garage {

    private int id;
    private String name;
    private Location location;
    private String availableHours;
    private ArrayList<OutCityTransport> vehicles;

    public Garage(int id, String name, Location location, String availableHours, ArrayList<OutCityTransport> vehicles)
    {
        this.id = id;
        this.name = name;
        this.location = location;
        this.availableHours = availableHours;
        this.vehicles = vehicles;
    }

    public Garage(int id, String name, Location location, String availableHours)
    {
        this.id = id;
        this.name = name;
        this.location = location;
        this.availableHours = availableHours;
        this.vehicles = new ArrayList<>();
    }

    public Garage(int id, String name, String address, Coordinates coords, String availableHours, ArrayList<OutCityTransport> vehicles)
    {
        this.id = id;
        this.name = name;
        this.location = new Location(coords, address);
        this.availableHours = availableHours;
        this.vehicles = vehicles;
    }

    public Garage(int id, String name, String address, Coordinates coords, String availableHours)
    {
        this.id = id;
        this.name = name;
        this.location = new Location(coords, address);
        this.availableHours = availableHours;
        this.vehicles = new ArrayList<>();
    }

    public int getId()
    {
        return id;
    }

    public Location getLocation()
    {
        return location;
    }

    public Coordinates getCoords()
    {
        return new Coordinates(location.getLat(), location.getLng());
    }

    public String getAddress()
    {
        return location.getAddress();
    }

    public String getName()
    {
        return name;
    }

    public ArrayList<OutCityTransport> getVehicles()
    {
        return vehicles;
    }
}
