package com.ceid.model.transport;

import com.ceid.model.service.GasStation;
import com.ceid.util.*;
import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;

public class Tracker implements Serializable
{
    private Coordinates coords;

    public Tracker(double lat,double lng){
        coords= new Coordinates(lat,lng);
    }

    public Tracker(Coordinates coords)
    {
        this.coords= coords;
    }

    public Tracker(LatLng coords)
    {
        this.coords = new Coordinates(coords);
    }

    public void getCoordsFromDatabase(){
        //get things from api call
    }

    public Coordinates getCoords()
    {
        return coords;
    }

    public void setCoords(Coordinates coords)
    {
        this.coords = coords;
    }
}
