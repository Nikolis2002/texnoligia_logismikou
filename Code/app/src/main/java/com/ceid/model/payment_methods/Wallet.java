package com.ceid.model.payment_methods;

import com.ceid.util.DateFormat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Wallet implements Serializable {
    private ArrayList<Card> cards=new ArrayList<>();
    private double cash;

    public Wallet(Card card,double cash) {
        cards.add(card);
        this.cash= cash;
    }

    public Wallet(double cash) {
        this.cash=cash;
    }

    public void addCard(Card card){
        cards.add(card);
    }
    public List<Card> getCards() {
        return cards;
    }
    public double getBalance() {
        return cash;
    }

    public void addToWallet(double cash) {
        this.cash=getBalance()+cash;
    }

    public void withdraw(double cash)
    {
        this.cash -= cash;
    }

    public void setBalance(double balance)
    {
        this.cash = balance;
    }

    public boolean isOverdrawn()
    {
        return (cash < 0);
    }
}
