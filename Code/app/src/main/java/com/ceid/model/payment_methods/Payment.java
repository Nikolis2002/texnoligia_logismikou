package com.ceid.model.payment_methods;
import com.ceid.model.users.Customer;
import com.ceid.util.PositiveInteger;

import java.io.Serializable;

public class Payment implements Serializable
{

    private double amount;
    public enum Method implements Serializable {WALLET,CASH};
    private Method method;

    public Payment(double amount, Method method) {
        this.amount = amount;
        this.method = method;
    }

    public Payment(Method method) {
        this.amount = -1;
        this.method = method;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String toString()
    {
        return String.format("%.02f€", amount);
    }
}

