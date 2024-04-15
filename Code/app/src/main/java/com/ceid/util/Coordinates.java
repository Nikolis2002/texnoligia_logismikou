package com.ceid.util;

public class Coordinates {
    private double x;
    private double y;

    public Coordinates(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void addCoords(Coordinates coord){
        this.x=this.x +coord.x;
        this.y=this.y + coord.y;
    }

    public void subCoords(Coordinates coord){
        this.x=this.x -coord.x;
        this.y=this.y - coord.y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
