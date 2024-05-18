package com.ceid.model.payment_methods;
import com.ceid.util.DateFormat;

public class Card {
    private String cardnumber;
    private String cardholderName;
    private String expirationDate;
    private String cvv;
    private String type;

    public Card(String number, String name, String Date, String cvv, String cardType){
        this.cardnumber=number;
        this.cardholderName=name;
        this.expirationDate=Date;
        this.cvv=cvv;
    }

    protected void setType(String type){
        this.type=type;
    }

    public void changeInfo(String number,String name,String Date,String cvv){
        this.cardnumber=number;
        this.cardholderName=name;
        this.expirationDate=Date;
        this.cvv=cvv;
    }

    // Getter for cardnumber
    public String getCardnumber() {
        return cardnumber;
    }

    // Getter for cardholderName
    public String getCardholderName() {
        return cardholderName;
    }

    // Getter for expirationDate
    public String getExpirationDate() {
        return expirationDate;
    }

    // Getter for cvv
    public String getCvv() {
        return cvv;
    }

    // Getter for type
    public String getType() {
        return type;
    }

    
}
