package com.ceid.util;

import java.time.LocalDate;
import java.time.DateTimeException;
import java.sql.Date;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

//our own primitive date type to help us with programming the rest of the app

public class DateFormat {
    private int day;
    private int month;
    private int year;
    private LocalDate localDate;

    public DateFormat(int date,int month,int year){
        if(!isValid(day,month,year))
            throw new IllegalArgumentException("Not a valid date");

        this.day=day;
        this.month=month;
        this.year=year;
    }

    public DateFormat(String dateString) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            this.localDate = LocalDate.parse(dateString, formatter);
            this.year = localDate.getYear();
            this.month = localDate.getMonthValue();
            this.day = localDate.getDayOfMonth();
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Not a valid date string: " + dateString);
        }
    }

    public DateFormat(int date,int month,int year,LocalDate localDate){

        this.day=day;
        this.month=month;
        this.year=year;
        this.localDate=localDate;
    }

    private boolean isValid(int day,int month,int year){
        try {
            localDate=LocalDate.of(year, month, day);
            return true; 
        } catch (DateTimeException e) {
            return false; 
        }
    }

     public String toSqlDateString() {
        return String.format("%04d-%02d-%02d", year, month, day);
    }

}
