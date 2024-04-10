package com.ceid.model.payment_methods;

import com.ceid.model.payment_methods.Card;
import com.ceid.model.payment_methods.Credit;
import com.ceid.model.payment_methods.Debit;
import com.ceid.util.DateFormat;

public class Wallet {
    private Card card ;
    private Currency cash;

    public Wallet(String number,String name,DateFormat Date,String cvv,String type,Number cash){
        if(type=="Credit"){
            card = new Credit(number,name,Date,cvv);
        }
        else
            card = new Debit(number,name,Date,cvv);

        this.cash=new Currency(cash);
    }

    public Wallet(String number,String name,DateFormat Date,String cvv,String type,Float cash){
        if(type=="Credit"){
            card = new Credit(number,name,Date,cvv);
        }
        else
            card = new Debit(number,name,Date,cvv);

        this.cash=new Currency(cash);
    }

    public Wallet(String number,String name,DateFormat Date,String cvv,String type,Double cash){ 
        if(type=="Credit"){
            card = new Credit(number,name,Date,cvv);
        }
        else
            card = new Debit(number,name,Date,cvv);

        this.cash=new Currency(cash);
    }

    public Card getCard(){
        return this.card;
    }

    public Currency getMoney(){
        return this.cash;
    }



    public void changeCardCredentials(String number,String name,DateFormat Date,String cvv){
        this.card.changeInfo(number,name,Date,cvv);
    }

    public void insertMoneytoWallet(){
        //api call to bank
    }

}
