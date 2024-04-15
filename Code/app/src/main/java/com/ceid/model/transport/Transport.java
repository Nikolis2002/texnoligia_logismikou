package com.ceid.model.transport;

import com.ceid.model.payment_methods.typeOfCurrency;
import com.ceid.util.*;

public class Transport {
    private String model;
    private String id;
    private String manufacturer;
    private DateFormat manuf_date;
    private String licence_plate;
    private boolean free_status;
    //field for Tracker
    private PositiveInteger seat_capacity;
    private typeOfCurrency rental_rate;

    public Transport(String id, String model, String manufacturer, DateFormat manuf_date, String licence_plate, boolean free_status, PositiveInteger seat_capacity, typeOfCurrency rental_rate) {
        this.id = id;
        this.model = model;
        this.manufacturer = manufacturer;
        this.manuf_date = manuf_date;
        this.licence_plate = licence_plate;
        this.free_status = free_status;
        this.seat_capacity = seat_capacity;
        this.rental_rate = rental_rate;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public DateFormat getManuf_date() {
        return manuf_date;
    }

    public void setManuf_date(DateFormat manuf_date) {
        this.manuf_date = manuf_date;
    }

    public String getLicence_plate() {
        return licence_plate;
    }

    public void setLicence_plate(String licence_plate) {
        this.licence_plate = licence_plate;
    }

    public boolean isFree_status() {
        return free_status;
    }

    public void setFree_status(boolean free_status) {
        this.free_status = free_status;
    }

    public PositiveInteger getSeat_capacity() {
        return seat_capacity;
    }

    public void setSeat_capacity(PositiveInteger seat_capacity) {
        this.seat_capacity = seat_capacity;
    }

    public typeOfCurrency getRental_rate() {
        return rental_rate;
    }

    public void setRental_rate(typeOfCurrency rental_rate) {
        this.rental_rate = rental_rate;
    }
}
