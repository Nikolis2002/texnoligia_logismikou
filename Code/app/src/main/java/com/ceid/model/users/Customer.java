package com.ceid.model.users;
import com.ceid.model.payment_methods.*;
public class Customer extends User {

    private Wallet wallet;
    //private int points=0;
    private CustomerHistory history;

    Customer(int id,String username,String name,String lname,String email,String info,Wallet wallet){
        super(id,username,name,lname,email);
        this.wallet=wallet;
    }

    /* 
    public int getPoints(){
        return this.points;
    }

    public void setPoints(int points){
        this.points=points;
    }
    */

    public CustomerHistory getHistory(){
        return this.history;
    }

    public Wallet getWallet(){
        return this.wallet;
    }
      
}
