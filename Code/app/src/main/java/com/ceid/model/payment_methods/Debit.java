package com.ceid.model.payment_methods;


public class Debit extends Card {

    public Debit(String number, String name, DateFormat Date, String cvv) {
        super(number, name, Date, cvv);
        super.setType("Debit");
    }
}
