package com.ceid.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.maps.model.LatLng;
import android.location.Location;
import android.util.Log;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Coordinates implements Serializable
{
    protected double lat;
    protected double lng;

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

    public static Coordinates parseCoords(String coordsString){
        coordsString=coordsString.replace("POINT(", "").replace(")", "");
        String[] coords= coordsString.split(" ");
        double lat=Double.parseDouble(coords[0]);
        double lng=Double.parseDouble(coords[1]);
        return new Coordinates(lat,lng);
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

    public double estimateTaxiCost(Coordinates endPoint){
        float distance=this.distance(endPoint);

        return 0.002 * distance;
    }

    public String coordsToJson(){
        Map<String, Double> map=new HashMap<>();
        map.put("lat", this.lat);
        map.put("lng", this.lng);
        String jsonString;
        ObjectMapper mapper = new ObjectMapper();
        try {
            jsonString=mapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return jsonString;
    }


}
