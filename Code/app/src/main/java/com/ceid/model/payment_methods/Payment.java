package com.ceid.model.payment_methods;
import androidx.annotation.NonNull;

import com.ceid.model.users.Customer;
import com.ceid.util.PositiveInteger;

import java.io.Serializable;

public class Payment implements Serializable
{

    private double amount;

    public enum Method implements Serializable
    {
        WALLET,CASH;

        public static Method fromString(String str)
        {
            if (str.equalsIgnoreCase("wallet"))
            {
                return WALLET;
            }
            else if (str.equalsIgnoreCase("cash"))
            {
                return CASH;
            }

            return null;
        }

        @NonNull
        public String toString()
        {
            return this.name();
        }
    };

    private Method method;

    public Payment(double amount, Method method) {
        this.amount = amount;
        this.method = method;
    }

    public Payment(double amount, String paymentMethod)
    {
        this.amount = amount;
        this.method = Method.fromString(paymentMethod);
    }

    public Payment(Method method) {
        this.amount = -1;
        this.method = method;
    }

    public static Method setPaymentType(String text) {
        if (text.equals("WALLET")) {
            return Method.WALLET;
        } else {
            return Method.CASH;
        }
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

