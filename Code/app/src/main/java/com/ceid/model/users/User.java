package com.ceid.model.users;

public class User {
    private String username;
    private String name;
    private String password;
    private String lname;
    private String email;

    public User(String username,String password,String name,String lname,String email){
        this.username=username;
        this.password=password;
        this.name=name;
        this.lname=lname;
        this.email=email;
    }
    public String usernameGetter(){
        return this.username;
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
}
