package com.ceid.model.users;

import android.util.Log;

import com.ceid.model.payment_methods.Card;
import com.ceid.model.payment_methods.Wallet;
import com.ceid.model.transport.Taxi;

public class TaxiDriver extends User{
    private boolean free_status;
    private Taxi taxi;
    public TaxiDriver(String username, String password, String name, String lname, String email, Wallet wallet, boolean free_status, Taxi taxi) {
        super(username,password, name, lname, email,wallet);
        this.free_status=free_status;
        this.taxi=taxi;
    }

    public Taxi getTaxi(){
        return this.taxi;
    }

    public boolean isFreeStatus() {
        return free_status;
    }

    @Override
    public void printUser() {
        Log.d("taxi", "Username: " + this.usernameGetter());
        Log.d("taxi", "Name: " + this.nameGetter());
        Log.d("taxi", "Last Name: " + this.lnameGetter());
        Log.d("taxi", "Email: " + this.emailGetter());
        Log.d("taxi", "Free Status: " + this.isFreeStatus());

        Taxi taxi = this.getTaxi();
        Log.d("taxi", "Taxi Plate Number: " + taxi.getLicence_plate());
        Log.d("taxi", "Taxi Model: " + taxi.getModel());
        Log.d("taxi", "Taxi Coords: " + taxi.getCoords().toString());

        Log.d("taxi", "Wallet Cards: ");
        for (Card card : this.getWallet().getCards()) {
            Log.d("taxi", "Card Number: " + card.getCardnumber());
            Log.d("taxi", "Card Holder: " + card.getCardholderName());
            Log.d("taxi", "Card Expiration Date: " + card.getExpirationDate());
            Log.d("taxi", "Card CVV: " + card.getCvv());
        }

        Log.d("taxi", "Wallet Balance: " + this.getWallet().getCash().toString());
    }
}
