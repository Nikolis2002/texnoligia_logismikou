package com.ceid.model.users;
import com.ceid.model.payment_methods.*;
import main.java.com.ceid.model.Points;
public class Customer extends User {

    private Wallet wallet;
    private Points points;
    private String licence;
    private CustomerHistory history;

    Customer(int id,String username,String name,String lname,String email,String info,Wallet wallet,String licence,int points){
        super(id,username,name,lname,email);
        this.wallet=wallet;
        this.licence=licence;
        this.points=new Points(points);
    }


    public CustomerHistory getHistory(){
        return this.history;
    }

    public Wallet getWallet(){
        return this.wallet;
    }

    public String get_licence(){
        return this.licence;
    }

    public void set_licence(String licence){
        this.licence=licence;
    }

    public Points get_points(){
        return this.points;
    }

    public  void set_points(int value){
        this.points.setPoints(value);
    }
      
}
