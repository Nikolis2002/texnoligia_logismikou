package com.ceid.model.users;
import com.ceid.model.service.Service;

import java.io.Serializable;
import java.util.ArrayList;

public class CustomerHistory implements Serializable {
    private ArrayList<Service> history = null;

    public CustomerHistory(){
        //get history from api
    }

    public ArrayList<Service> getList()
    {
        return history;
    }
}
