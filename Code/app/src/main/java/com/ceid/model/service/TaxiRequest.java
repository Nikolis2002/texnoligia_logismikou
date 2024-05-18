package com.ceid.model.service;

import com.ceid.model.payment_methods.Payment;
import com.ceid.model.transport.Transport;
import com.ceid.model.users.TaxiDriver;
import com.ceid.util.Coordinates;

import java.time.LocalDateTime;


public class TaxiRequest
{
    private int id;
    private Coordinates pickupLocation;
    private Coordinates destination;
    private String paymentMethod;
    private TaxiDriver taxiDriver = null;
    private LocalDateTime assignmentTime = null;
    private LocalDateTime pickupTime = null;

    public TaxiRequest(Coordinates pickupLocation, Coordinates destination, String paymentMethod) {
        this.pickupLocation = pickupLocation;
        this.destination = destination;
        this.paymentMethod=paymentMethod;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Coordinates getPickupLocation()
    {
        return pickupLocation;
    }

    public Coordinates getDestination()
    {
        return destination;
    }

    public TaxiDriver getTaxiDriver()
    {
        return taxiDriver;
    }

    public LocalDateTime getAssignmentTime()
    {
        return assignmentTime;
    }

    public LocalDateTime getPickupTime()
    {
        return pickupTime;
    }

    public void setTaxiDriver(TaxiDriver taxiDriver)
    {
        this.taxiDriver = taxiDriver;
    }

    public void setAssignmentTime(LocalDateTime time)
    {
        this.assignmentTime = time;
    }

    public void setPickupTime(LocalDateTime time)
    {
        this.pickupTime = time;
    }
}
