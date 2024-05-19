package com.ceid.model.users;

import com.ceid.model.payment_methods.Wallet;

import java.io.Serializable;

public class User implements Serializable
{
    private String username;
    private String name;
    private String password;
    private String lname;
    private String email;
    private Wallet wallet;

    public User(String username,String password,String name,String lname,String email,Wallet wallet){
        this.username=username;
        this.password=password;
        this.name=name;
        this.lname=lname;
        this.email=email;
        this.wallet=wallet;
    }
    public String usernameGetter(){
        return this.username;
    }

    public Wallet getWallet(){
        return this.wallet;
    }
    public String nameGetter(){
        return this.name;
    }

    public String lnameGetter(){
        return this.lname;
    }

    public String emailGetter(){
        return this.email;
    }

    protected void usernameSetter(String Username){
         this.username=Username;
    }

    protected void nameSetter(String name){
         this.name=name;
    }

    protected void lnameSetter(String lname){
         this.lname=lname;
    }

    protected void emailSetter(String email){
         this.email=email;
    }

    public void printUser(){}
}
