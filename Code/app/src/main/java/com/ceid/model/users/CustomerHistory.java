package com.ceid.model.users;
import com.ceid.model.service.Service;

import java.io.Serializable;
import java.util.ArrayList;

public class CustomerHistory implements Serializable {
    private ArrayList<Service> history = null;

    public CustomerHistory(ArrayList<Service> history){
        this.history = history;
    }

    public CustomerHistory(){
        this.history = new ArrayList<Service>();
    }

    public ArrayList<Service> getList()
    {
        return history;
    }

    public void setList(ArrayList<Service> history)
    {
        this.history = history;
    }

    public void add(Service service)
    {
        //If history is empty, don't add anything.
        //The history will be retrieved fully from the database when you go to the history screen anyway
        if (history.isEmpty())
            return;

        this.history.add(0, service);
    }

    public boolean retrieved()
    {
        return !history.isEmpty();
    }
}
