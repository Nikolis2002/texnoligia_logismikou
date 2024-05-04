package com.ceid.model.users;
import com.ceid.util.PositiveInteger;

public class Points {
    PositiveInteger points;

public Points(int points){
    this.points=new PositiveInteger(points);
}

public PositiveInteger getPoints(){
    return this.points;
}

public void setPoints(int points){
    this.points.setValue(points);
}
}
