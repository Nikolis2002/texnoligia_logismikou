package com.ceid.util;

public class Location extends Coordinates{

    private final String Address;

    public Location(double lat, double lng, String AddressName) {
        super(lat, lng);
        this.Address = AddressName;
    }

    public Location(Coordinates coords, String AddressName) {
        super(coords);
        this.Address = AddressName;
    }

    public String getAddress(){
        return Address;
    }

}
