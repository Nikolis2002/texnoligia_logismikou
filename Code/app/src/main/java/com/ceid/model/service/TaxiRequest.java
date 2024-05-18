package com.ceid.model.service;

import com.ceid.model.payment_methods.Payment;
import com.ceid.model.transport.Transport;
import com.ceid.model.users.TaxiDriver;
import com.ceid.util.Coordinates;

import java.time.LocalDateTime;

public class TaxiRequest{

    private int id;
    private Coordinates pickUpLocation;
    private Coordinates destination;
    private TaxiDriver taxiDriver;
    private LocalDateTime assignmentTime;
    private LocalDateTime pickUpTime;
    private TaxiService taxiService;
    private TaxiRequestProgress requestProgress;

    public TaxiRequest(int id, Coordinates pickUpLocation, Coordinates destination, TaxiDriver taxiDriver, LocalDateTime assignmentTime, LocalDateTime pickUpTime, TaxiService taxiService, TaxiRequestProgress requestProgress) {
        this.id = id;
        this.pickUpLocation = pickUpLocation;
        this.destination = destination;
        this.taxiDriver = taxiDriver;
        this.assignmentTime = assignmentTime;
        this.pickUpTime = pickUpTime;
        this.taxiService = taxiService;
        this.requestProgress = requestProgress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Coordinates getPickUpLocation() {
        return pickUpLocation;
    }

    public void setPickUpLocation(Coordinates pickUpLocation) {
        this.pickUpLocation = pickUpLocation;
    }

    public Coordinates getDestination() {
        return destination;
    }

    public void setDestination(Coordinates destination) {
        this.destination = destination;
    }

    public TaxiDriver getTaxiDriver() {
        return taxiDriver;
    }

    public void setTaxiDriver(TaxiDriver taxiDriver) {
        this.taxiDriver = taxiDriver;
    }

    public LocalDateTime getAssignmentTime() {
        return assignmentTime;
    }

    public void setAssignmentTime(LocalDateTime assignmentTime) {
        this.assignmentTime = assignmentTime;
    }

    public LocalDateTime getPickUpTime() {
        return pickUpTime;
    }

    public void setPickUpTime(LocalDateTime pickUpTime) {
        this.pickUpTime = pickUpTime;
    }

    public TaxiService getTaxiService() {
        return taxiService;
    }

    public void setTaxiService(TaxiService taxiService) {
        this.taxiService = taxiService;
    }

    public TaxiRequestProgress getRequestProgress() {
        return requestProgress;
    }

    public void setRequestProgress(TaxiRequestProgress requestProgress) {
        this.requestProgress = requestProgress;
    }
}
