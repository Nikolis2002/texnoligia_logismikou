package com.ceid.model.users;

import java.io.Serializable;

public class Points implements Serializable {
    private int points;

    public Points(int points){
        this.points= points;
    }

    public int getPoints(){
        return this.points;
    }

    public void addPoints(int points){
        this.points += points;
    }

    public void setPoints(int points){
        this.points+=points;
    }

    public static int calculatePoints(double cost){
        return (int) (cost*0.5);
    }

}
