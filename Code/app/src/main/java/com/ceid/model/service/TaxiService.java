package com.ceid.model.service;

import com.ceid.model.payment_methods.Payment;
import com.ceid.model.transport.Taxi;
import com.ceid.model.transport.Transport;
import com.ceid.util.DateFormat;
import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDateTime;

public class TaxiService extends Service {

    private TaxiRequest request;

    public TaxiService(int id, LocalDateTime creationDate, Payment payment, Rating rating, int earnedPoints, Transport transport, TaxiRequest request) {
        super(id, creationDate, payment, rating, earnedPoints, transport);

        this.request = request;
    }

    public TaxiService(int id, Payment payment){
        super(id,payment);
    }

    public TaxiService(JsonNode data)
    {
        super(
                data.get("id").asInt(),
                DateFormat.parseFromJS(data.get("creation_date").asText()),
                new Payment(
                        data.get("amount").asDouble(),
                        data.get("payment_method").asText()
                ),
                Rating.makeRating(data.get("rating")),
                data.get("earned_points").asInt(),
                new Taxi(data.get("vehicle"))
        );

        this.request = new TaxiRequest(data.get("taxi_request"), Payment.Method.fromString(data.get("payment_method").asText()));
    }

    public TaxiRequest getRequest()
    {
        return request;
    }

}