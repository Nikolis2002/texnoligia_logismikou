package com.ceid.model.users;
import android.util.Log;

import com.ceid.model.payment_methods.*;
import com.google.gson.Gson;

public class Customer extends User {

    private Points points;
    private byte[] img;

    private String license;
    private CustomerHistory history;

   public Customer(String username,String password,String name,String lname,String email,byte[] img,Wallet wallet,String license,int points){
        super(username,password,name,lname,email,wallet);
        this.license=license;
        this.img=img;
        this.points=new Points(points);
    }

    public CustomerHistory getHistory(){
        return this.history;
    }

    public String getLicense(){
        return this.license;
    }

    public void setLicense(String license){
        this.license = license;
    }

    public Points getPoints(){
        return this.points;
    }

    public  void setPoints(int value){
        this.points.setPoints(value);
    }

    public String convertCustomerToJsonString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    @Override
    public void printUser() {
        Log.d("Customer", "Username: " + this.getUsername());
        Log.d("Customer", "Name: " + this.getName());
        Log.d("Customer", "Last Name: " + this.getLname());
        Log.d("Customer", "Email: " + this.getEmail());
        Log.d("Customer", "License: " + this.getLicense());
        Log.d("Customer", "Points: " + this.getPoints().getPoints());

        // Log wallet details
        Log.d("Customer", "Wallet Cards: ");
        for (Card card : this.getWallet().getCards()) {
            Log.d("Customer", "Card Number: " + card.getCardnumber());
            Log.d("Customer", "Card Holder: " + card.getCardholderName());
            Log.d("Customer", "Card Expiration Date: " + card.getExpirationDate());
            Log.d("Customer", "Card CVV: " + card.getCvv());
        }

        Log.d("Customer", "Wallet Balance: " + this.getWallet().getCash().toString());

        // Log history if needed
        // Log.d("Customer", "History: " + this.history.toString()); // Assuming CustomerHistory has a meaningful toString method
    }
      
}
