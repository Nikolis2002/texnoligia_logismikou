package com.ceid.model.users;

import com.ceid.model.payment_methods.Wallet;

import java.io.Serializable;

public class User implements Serializable
{
    private static User currentUser;

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
    public String getUsername(){
        return this.username;
    }

    public Wallet getWallet(){
        return this.wallet;
    }

    public String getName(){
        return this.name;
    }

    public String getLname(){
        return this.lname;
    }

    public String getLastname(){
    return this.lname;
}

    public String getEmail(){
        return this.email;
    }

    protected void setUsername(String Username){
         this.username=Username;
    }

    protected void setName(String name){
         this.name=name;
    }

    protected void setLname(String lname){
         this.lname=lname;
    }

    protected void setLastname(String lname){
        this.lname=lname;
    }

    protected void setEmail(String email){
         this.email=email;
    }

    public void printUser(){}

    public static void setCurrentUser(User user)
    {
        User.currentUser = user;
    }

    public static User currentUser()
    {
        return User.currentUser;
    }

    public static User getCurrentUser()
    {
        return User.currentUser;
    }
}
