package com.ceid.model.payment_methods;

public class Credit extends Card {

    public Credit(String number, String name, DateFormat Date, String cvv) {
        super(number, name, Date, cvv);
        super.setType("Credit");
    }
}
