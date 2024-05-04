package com.ceid.util;

public class PositiveInteger {
    private int value;

    public PositiveInteger(int value){
        setValue(value);
    }

    public int getValue(){
        return this.value;
    }

    public void setValue(int value){

        if(value>=0){
            this.value=value;
        }
        else{
            throw new IllegalArgumentException("NOT A POSITIVE INTEGER");
        }
    }
}