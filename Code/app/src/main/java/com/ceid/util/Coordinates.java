package com.ceid.util;

import com.google.android.gms.maps.model.LatLng;
import android.location.Location;
import android.util.Log;

import java.io.Serializable;

public class Coordinates implements Serializable
{
    private double lat;
    private double lng;

    public Coordinates(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

	public Coordinates(Coordinates coords)
	{
        this.lat = coords.lat;
        this.lng = coords.lng;
	}

    public Coordinates(LatLng coords)
    {
        this.lat = coords.latitude;
        this.lng = coords.longitude;
    }

	public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public void addCoords(Coordinates coord){
        this.lat=this.lat +coord.lat;
        this.lng=this.lng + coord.lng;
    }

    public void subCoords(Coordinates coord){
        this.lat=this.lat -coord.lat;
        this.lng=this.lng - coord.lng;
    }

    @Override
    public String toString() {
        return String.format("(%.7f, %.7f)", lat, lng);
    }

    public LatLng toLatLng()
    {
        return new LatLng(this.lat, this.lng);
    }

    public boolean withinRadius(Coordinates other, float radius)
    {
        Location center = new Location("center");
        center.setLatitude(this.lat);
        center.setLongitude(this.lng);

        Location point = new Location("point");
        point.setLatitude(other.getLat());
        point.setLongitude(other.getLng());

        float dista = center.distanceTo(point);

        Log.d("DISTA", String.format("%f", dista));

        System.out.println("testtesttest");

        return dista <= radius;
    }

    public float distance(Coordinates other)
    {
        Location myloc = new Location("myloc");
        myloc.setLatitude(this.lat);
        myloc.setLongitude(this.lng);

        Location point = new Location("point");
        point.setLatitude(other.getLat());
        point.setLongitude(other.getLng());

        return myloc.distanceTo(point);
    }
}
