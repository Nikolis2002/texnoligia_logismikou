package com.ceid.model.service;

import com.ceid.model.payment_methods.Payment;
import com.ceid.model.transport.Transport;

import java.io.Serializable;
import java.time.LocalDateTime;

public abstract class Service implements Serializable
{
    private int id;
    private LocalDateTime creationDate;
    private Payment payment;
    private Rating rating = null;
    private Transport transport;
    private int earnedPoints = 0;

    /*
    public Service(LocalDateTime creationDate, Payment payment, Rating rating, Transport transport) {
        this.creationDate = creationDate;
        this.payment = payment;
        this.rating = rating;
        this.transport = transport;
    }*/

    public Service(int id, LocalDateTime creationDate, Payment payment, Rating rating, int earnedPoints, Transport transport) {
        this.id = id;
        this.creationDate = creationDate;
        this.payment = payment;
        this.transport = transport;
        this.earnedPoints = earnedPoints;
    }


    public Service(int id, Payment payment) {
        this.id=id;
        this.payment=payment;
    }

    public Rating rate(Float vehicleStars, Float otherStars, String comment)
    {

        if (this instanceof RentalService)
        {
            rating = new Rating(comment, vehicleStars.intValue());
        }
        else if (this instanceof OutCityService)
        {
            rating = new Rating(comment, vehicleStars.intValue(), otherStars.intValue(), RatingType.OUTCITY);
        }
        else
        {
            rating = new Rating(comment, vehicleStars.intValue(), otherStars.intValue(), RatingType.TAXI);
        }

        return rating;
    }

    public Rating getRating()
    {
        return rating;
    }

    public Transport getTransport()
    {
        return transport;
    }

    public LocalDateTime getCreationDate()
    {
        return creationDate;
    }

    public int getId()
    {
        return id;
    }

    public Payment getPayment()
    {
        return payment;
    }

    public void addPoints(int points)
    {
        this.earnedPoints += points;
    }

    public int getPoints()
    {
        return earnedPoints;
    }

    public Payment.Method getPaymentMethod()
    {
        return payment.getMethod();
    }
}
