package com.ceid.model.users;
import com.ceid.model.service.Service;

import java.io.Serializable;
import java.util.ArrayList;

public class CustomerHistory implements Serializable {
    private ArrayList<Service> history = null;

    public CustomerHistory(ArrayList<Service> history){
        this.history = history;
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
        this.history.add(0, service);
    }
}
