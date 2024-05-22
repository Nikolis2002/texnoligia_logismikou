package com.ceid.model.payment_methods;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.math.BigDecimal;

public class Currency implements Serializable
{
    private BigDecimal value;
    CurrencyType currency= CurrencyType.EURO; //default value

    //TODO:make the methods better and make more setters and getters

    public Currency(Number value){
        this.value=BigDecimal.valueOf(value.doubleValue());
    }

    public Currency(Float value){
        this.value=new BigDecimal(value.toString());
    }

    public Currency(Double value){
        this.value=BigDecimal.valueOf(value);
    }

    public Currency(String value){
        this.value=new BigDecimal(value);
    }

    public Currency setCurrency(String currency){
        if(currency=="$"||currency=="Dollar"){
            this.currency= CurrencyType.DOLLAR;
        }
        else
          this.currency= CurrencyType.EURO;

        return this;
    }

    public double getValue(){
        return this.value.doubleValue();
    }

    public String getCurrencySymbol(){
        return this.currency.getSymbol();
    }

    public String getCurrencyName(){
        return this.currency.getName();
    }

    @NonNull
    public String toString()
    {
        if (currency == CurrencyType.EURO)
        {
            return String.format("%s%s", value.toString(),currency.getSymbol());
        }
        else
        {
            return String.format("%s%s",currency.getSymbol(), value.toString());
        }

    }

    //for numeric types like Integer, Long, Short, Byte
    public Currency mulValue(Number value){
        BigDecimal tmp=BigDecimal.valueOf(value.doubleValue());
        return new Currency(this.value.multiply(tmp)).setCurrency(this.currency.getSymbol());
    }

    //same for float and double
    public Currency mulValue(Float value){
        BigDecimal tmp=new BigDecimal(value.toString());
        return new Currency(this.value.multiply(tmp)).setCurrency(this.currency.getSymbol());
    }

    public Currency mulValue(Double value){
        BigDecimal tmp= BigDecimal.valueOf(value);
        return new Currency(this.value.multiply(tmp)).setCurrency(this.currency.getSymbol());
    }
    
    ////////////////////////////////////////////////////////////add
    public Currency addValue(Number value){
        BigDecimal tmp=BigDecimal.valueOf(value.doubleValue());
        return new Currency(this.value.add(tmp)).setCurrency(this.currency.getSymbol());
    }

    //same for float and double
    public Currency addValue(Float value){
        BigDecimal tmp=new BigDecimal(value.toString());
        return new Currency(this.value.add(tmp)).setCurrency(this.currency.getSymbol());
    }

    public Currency addValue(Double value){
        BigDecimal tmp= BigDecimal.valueOf(value);
        return new Currency(this.value.add(tmp)).setCurrency(this.currency.getSymbol());
    }

    ////////////////////////////////////////////////////////for someone TODO:make the same for division

}


