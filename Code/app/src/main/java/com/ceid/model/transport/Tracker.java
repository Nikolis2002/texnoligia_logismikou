package com.ceid.model.transport;

import com.ceid.util.*;


public class Tracker {
    private Coordinates coords;

    public Tracker(double x,double y){
        coords= new Coordinates(x,y);
    }

    public void getCoordsFromDatabase(){
        //get things from api call
    }

    public void addTrackerCoords(Coordinates coords){
            this.coords.addCoords(coords);
    }

    public void subTrackerCoords(Coordinates coords){
        this.coords.subCoords(coords);
    }
    
    //TODO:other methods for tracker usage !
}
