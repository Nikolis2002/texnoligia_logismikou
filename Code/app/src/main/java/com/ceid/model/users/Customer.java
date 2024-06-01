package com.ceid.model.users;
import android.util.Log;

import com.ceid.model.payment_methods.*;
import com.ceid.model.service.Service;
import com.google.gson.Gson;

import java.util.ArrayList;

public class Customer extends User {

    private Points points;
    private byte[] img = null;

    private String license;
    private CustomerHistory history = new CustomerHistory();

   public Customer(String username,String password,String name,String lname,String email,byte[] img,Wallet wallet,String license,int points){
        super(username,password,name,lname,email,wallet);
        this.license=license;
        this.img=img;
        this.points=new Points(points);
    }

    public CustomerHistory getHistory(){
        return this.history;
    }

    public void setHistory(CustomerHistory history)
    {
        this.history = history;
    }

    public void setHistory(ArrayList<Service> history)
    {
        this.history = new CustomerHistory(history);
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

    public void addPoints(int points){
        this.points.addPoints(points);
    }

    public void subtractPoints(int points){
        this.points.subtractPoints(points);
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

        Log.d("Customer", "Wallet Balance: " + this.getWallet().getBalance());

        // Log history if needed
        // Log.d("Customer", "History: " + this.history.toString()); // Assuming CustomerHistory has a meaningful toString method
    }

	public void setLicenseImg(byte[] img)
	{
        this.img = img;
	}

    public boolean hasLicense()
    {
        return img != null;
    }

    public byte[] getLicenseImg()
    {
        return img;
    }
}
