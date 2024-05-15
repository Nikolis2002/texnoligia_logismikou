package com.ceid.model.transport;
import android.os.Parcel;
import android.os.Parcelable;

import com.ceid.util.Coordinates;
import com.ceid.util.Location;

import java.io.Serializable;
import java.util.ArrayList;

public class Garage implements Serializable {

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

    /*
    //Parcelable
    //========================================================================

    public Garage(Parcel in)
    {
        id = in.readInt();
        name = in.readString();
        location = in.readParcelable(Location.class.getClassLoader());
        availableHours = in.readString();
        in.readTypedList(vehicles, OutCityTransport.CREATOR);
    }

    public static final Parcelable.Creator<Garage> CREATOR = new Parcelable.Creator<Garage>() {
        @Override
        public Garage createFromParcel(Parcel source) {
            return new Garage(source);
        }


        @Override
        public Garage[] newArray(int size) {
            return new Garage[size];
        }
    };

    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeParcelable(location, 0);
        dest.writeString(availableHours);
        dest.writeTypedList(vehicles);
    }

    @Override
    public int describeContents() {
        return 0;
    }
    */
}
