package com.ceid.model.service;

import androidx.annotation.NonNull;

import com.ceid.model.payment_methods.Payment;
import com.ceid.model.transport.Taxi;
import com.ceid.model.transport.Transport;
import com.ceid.model.users.TaxiDriver;
import com.ceid.util.Coordinates;
import com.ceid.util.DateFormat;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.Serializable;
import java.time.LocalDateTime;


public class TaxiRequest implements Serializable
{
    private int id;
    private Coordinates pickupLocation;
    private Coordinates destination;
    private Payment.Method payment;
    private LocalDateTime assignmentTime = null;
    private LocalDateTime pickupTime = null;

    public TaxiRequest(int id, Coordinates pickUp, Coordinates dest, Payment.Method payment) {
        this.id=id;
        this.pickupLocation=pickUp;
        this.destination=dest;
        this.payment= payment;
    }

    public TaxiRequest(JsonNode requestData, Payment.Method paymentMethod)
    {
        this.id = requestData.get("id").asInt();
        this.pickupLocation = new Coordinates(requestData.get("pickup_location"));
        this.destination = new Coordinates(requestData.get("destination"));
        this.assignmentTime = DateFormat.parseFromJS(requestData.get("assignment_time").asText());
        this.pickupTime = DateFormat.parseFromJS(requestData.get("pickup_time").asText());
        this.payment = paymentMethod;
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

    /*public TaxiDriver getTaxiDriver()
    {
        return taxiDriver;
    }*/

    public LocalDateTime getAssignmentTime()
    {
        return assignmentTime;
    }

    public LocalDateTime getPickupTime()
    {
        return pickupTime;
    }

    /*public void setTaxiDriver(TaxiDriver taxiDriver)
    {
        this.taxiDriver = taxiDriver;
    }*/

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

    public String calculateEta(Coordinates taxiCoords)
    {
        float distance=this.pickupLocation.distance(taxiCoords);
        double timeCalc= (distance*0.1)/60;

        return String.format("%.2f", timeCalc);
    }
}
