package com.ceid.model.service;

import androidx.annotation.NonNull;

import com.ceid.model.payment_methods.Payment;
import com.ceid.model.transport.Transport;
import com.ceid.model.users.TaxiDriver;
import com.ceid.util.Coordinates;

import java.io.Serializable;
import java.time.LocalDateTime;


public class TaxiRequest implements Serializable
{
    private int id;
    private Coordinates pickupLocation;
    private Coordinates destination;
    private Payment.Method payment;
    private TaxiDriver taxiDriver = null;
    private LocalDateTime assignmentTime = null;
    private LocalDateTime pickupTime = null;

    public TaxiRequest(Coordinates pickupLocation, Coordinates destination, Payment.Method payment) {
        this.pickupLocation = pickupLocation;
        this.destination = destination;
        this.payment=payment;
    }

    public TaxiRequest(int id, Coordinates pickUp, Coordinates dest, Payment.Method payment) {
        this.id=id;
        this.pickupLocation=pickUp;
        this.destination=dest;
        this.payment= payment;
    }

    public Payment.Method getPaymentMethod() {
        return payment;
    }

    public void setPaymentMethod(Payment.Method payment) {
        this.payment = payment;
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

    @NonNull
    @Override
    public String toString() {
        return String.format("TaxiRequest{id=%d, pickupLocation=%s, destination=%s, payment=%s}",
                id,
                pickupLocation.toString(),
                destination.toString(),
                payment.toString());
    }
}
