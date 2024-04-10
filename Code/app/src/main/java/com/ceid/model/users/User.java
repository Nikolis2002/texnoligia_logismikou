package com.ceid.model.users;

public class User {
    private int id;
    private String username;
    private String name;
    private String lname;
    private String email;

    public User(int id,String username,String name,String lname,String email){
        this.id=id;
        this.username=username;
        this.name=name;
        this.lname=lname;
        this.email=email;
    }

    public int idGetter(){
        return this.id;
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

    protected void idSetter(int id){
         this.id=id;
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
