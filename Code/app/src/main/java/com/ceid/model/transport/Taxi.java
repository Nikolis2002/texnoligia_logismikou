package com.ceid.model.transport;

import com.ceid.util.Coordinates;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Taxi extends InCityTransport {
    private String licence_plate;
    private Coordinates coords;

    public Taxi(int id, String model, String manufacturer, String manuf_year,String licence_plate,double lat,double lng)
    {
        super(id, model, manufacturer, manuf_year);
        this.licence_plate=licence_plate;
        this.coords=new Coordinates(lat,lng);
    }

    public Taxi(int id, String model, String manufacturer, String manuf_year,String licence_plate,Coordinates coords)
    {
        super(id, model, manufacturer, manuf_year);
        this.licence_plate=licence_plate;
        this.coords=coords;
    }

    public Taxi(int id, String model, String manufacturer, String manuf_year,String licence_plate)
    {
        super(id, model, manufacturer, manuf_year);
        this.licence_plate=licence_plate;
        this.coords=null;
    }


    public String getLicence_plate() {
        return licence_plate;
    }

    public void setLicence_plate(String licence_plate) {
        this.licence_plate = licence_plate;
    }

    public Coordinates getCoords() {
        return coords;
    }

    public void setCoords(Coordinates coords) {
        this.coords = coords;
    }

    public void gpsLocation(){

        List<Coordinates> coordinatesList= new ArrayList<>();

        coordinatesList.add(new Coordinates(
                38.223895,
                21.726609
        ));

        coordinatesList.add(new Coordinates(
                38.221536,
                21.750299
        ));

        coordinatesList.add(new Coordinates(
                38.22635678116209,
                21.73197852178154
        ));

        coordinatesList.add(new Coordinates(
                38.240719628934386,
                21.734417316103578
        ));

        coordinatesList.add(new Coordinates(
                38.2564928297534,
                21.743004198708753
        ));

        coordinatesList.add(new Coordinates(
                38.27987472095645,
                21.745407675714397
        ));

        Random random = new Random();
        int randomLocation = random.nextInt(coordinatesList.size());
        setCoords(coordinatesList.get(randomLocation));
    }
}
