package com.ceid.model.users;
import com.ceid.model.payment_methods.*;
import com.google.gson.Gson;

public class Customer extends User {

    private Wallet wallet;
    private Points points;
    private String license;
    private CustomerHistory history;

    Customer(int id,String username,String name,String lname,String email,String info,Wallet wallet,String license,int points){
        super(id,username,name,lname,email);
        this.wallet=wallet;
        this.license=license;
        this.points=new Points(points);
    }

    public CustomerHistory getHistory(){
        return this.history;
    }

    public Wallet getWallet(){
        return this.wallet;
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
      
}
