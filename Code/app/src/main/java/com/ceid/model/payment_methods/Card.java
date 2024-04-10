package com.ceid.model.payment_methods;
import com.ceid.util.DateFormat;

public abstract class Card {
    private String cardnumber;
    private String cardholderName;
    private DateFormat expirationDate;
    private String cvv;
    private String type;

    public Card(String number,String name,DateFormat Date,String cvv){
        this.cardnumber=number;
        this.cardholderName=name;
        this.expirationDate=Date;
        this.cvv=cvv;
    }

    protected void setType(String type){
        this.type=type;
    }

    public void changeInfo(String number,String name,DateFormat Date,String cvv){
        this.cardnumber=number;
        this.cardholderName=name;
        this.expirationDate=Date;
        this.cvv=cvv;
    }

    
}
