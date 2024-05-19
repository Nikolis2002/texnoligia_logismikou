package com.ceid.util;

import java.time.Duration;
import java.time.Instant;

public class Timer {

    public Timer() {
    }

    public Instant startTimer(){
        return Instant.now();
    }

    public Instant stopTimer(){
        return Instant.now();
    }

    public long elapsedTime(Instant start, Instant end){
        Duration elapsedTime = Duration.between(start,end);
        if(elapsedTime.toMinutes()<1){
            return elapsedTime.getSeconds();
        }else{
            return  elapsedTime.toMinutes();
        }

    }

}
