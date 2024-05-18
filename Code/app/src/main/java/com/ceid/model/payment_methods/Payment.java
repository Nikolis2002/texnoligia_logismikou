package com.ceid.model.payment_methods;
import com.ceid.model.users.Customer;
import com.ceid.util.PositiveInteger;

import java.io.Serializable;

public class Payment implements Serializable
{
    //private Customer person;

    public enum Method implements Serializable {WALLET,CASH};
    private Method method;
    private Currency amount;

    /*public Customer getPerson() {
        return person;
    }*/

    /*
    public void setPerson(Customer person) {
        this.person = person;
    }*/

    public Currency getAmount() {
        return amount;
    }

    public void setAmount(Currency amount) {
        this.amount = amount;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Payment(Currency amount,Method method){
        this.amount=amount;
        this.method=method;
    }

    public String toString()
    {
        return amount.toString();
    }
}

