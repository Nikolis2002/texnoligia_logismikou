package com.ceid.model.users;
import com.ceid.util.PositiveInteger;

import java.io.Serializable;

public class Points implements Serializable {
    int points;

    public Points(int points){
        this.points= points;
    }

    public int getPoints(){
        return this.points;
    }

    public void setPoints(int points){
        this.points+=points;
    }

    public int calcPoints(double cost){
        int point=(int) (cost*0.5);
        this.setPoints(point);
        return point;
    }

}
