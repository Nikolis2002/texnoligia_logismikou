package com.ceid.model.payment_methods;

import com.ceid.util.DateFormat;

import java.util.ArrayList;
import java.util.List;

public class Wallet {
    private ArrayList<Card> cards=new ArrayList<>();
    private Currency cash;

    public Wallet(Card card,double cash) {
        cards.add(card);
        this.cash=new Currency(cash);
    }

    public Wallet(double cash) {
        this.cash=new Currency(cash);
    }

    public void addCard(Card card){
        cards.add(card);
    }
    public List<Card> getCards() {
        return cards;
    }
    public Currency getCash() {
        return cash;
    }
}
