package com.ceid.model.transport;
import com.ceid.model.payment_methods.typeOfCurrency;
import com.ceid.util.DateFormat;
import com.ceid.util.PositiveInteger;
import java.util.ArrayList;

public class CityTransport extends Transport {
    
    private String emmision;
    private ArrayList<String>  accessibilityFeatures;

    public String getEmmision() {
        return emmision;
    }

    public void setEmmision(String emmision) {
        this.emmision = emmision;
    }

    public ArrayList<String> getAccessibilityFeatures() {
        return accessibilityFeatures;
    }

    public void setAccessibilityFeatures(ArrayList<String> accessibilityFeatures) {
        this.accessibilityFeatures = accessibilityFeatures;
    }

    public CityTransport(String id, String model, String manufacturer, DateFormat manuf_date, String licence_plate, boolean free_status, PositiveInteger seat_capacity, typeOfCurrency rental_rate, String emmision, ArrayList<String> accessibilityFeatures) {
        super(id,  model,  manufacturer,  manuf_date, licence_plate, free_status,  seat_capacity, rental_rate);
        this.emmision = emmision;
        this.accessibilityFeatures = accessibilityFeatures;
    }

    

    
}
