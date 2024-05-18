package com.ceid.model.users;

import com.ceid.model.transport.Taxi;

public class TaxiDriver extends User{
    private boolean free_status;
    private Taxi taxi;
    public TaxiDriver( String username, String password,String name, String lname, String email,boolean free_status,Taxi taxi) {
        super(username,password, name, lname, email);
        this.free_status=free_status;
        this.taxi=taxi;
    }

    public Taxi getTaxi(){
        return this.taxi;
    }
}
