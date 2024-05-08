package com.ceid.model.payment_methods;

import androidx.annotation.NonNull;

import java.math.BigDecimal;

public class Currency {
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
    public void setCurrency(String currency){
        if(currency=="$"||currency=="Dollar"){
            this.currency= CurrencyType.DOLLAR;
        }
        else
          this.currency= CurrencyType.EURO;
    }

    public BigDecimal getValue(){
        return this.value;
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
    public void mulValue(Number value){
        BigDecimal tmp=BigDecimal.valueOf(value.doubleValue());
        this.value=this.value.multiply(tmp);
    }

    //same for float and double
    public void mulValue(Float value){
        BigDecimal tmp=new BigDecimal(value.toString());
        this.value=this.value.multiply(tmp);
    }

    public void mulValue(Double value){
        BigDecimal tmp= BigDecimal.valueOf(value);
        this.value=this.value.add(tmp);
    }
    
    ////////////////////////////////////////////////////////////add
    public void addValue(Number value){
        BigDecimal tmp=BigDecimal.valueOf(value.doubleValue());
        this.value=this.value.multiply(tmp);
    }

    //same for float and double
    public void addValue(Float value){
        BigDecimal tmp=new BigDecimal(value.toString());
        this.value=this.value.add(tmp);
    }

    public void addValue(Double value){
        BigDecimal tmp= BigDecimal.valueOf(value);
        this.value=this.value.add(tmp);
    }

    ////////////////////////////////////////////////////////for someone TODO:make the same for division

}


