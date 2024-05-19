package com.ceid.model.users;
import com.ceid.util.PositiveInteger;

import java.io.Serializable;

public class Points implements Serializable {
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
