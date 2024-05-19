package com.ceid.model.users;
import com.ceid.model.service.Service;

import java.io.Serializable;
import java.util.ArrayList;

public class CustomerHistory implements Serializable {
    private ArrayList<Service> history=new ArrayList<Service>();

    public CustomerHistory(){
        //get history from api
    }
}
