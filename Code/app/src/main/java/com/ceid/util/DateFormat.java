package com.ceid.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateFormat {

    private static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static DateTimeFormatter datetimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    //Format
    //========================================================================

    public static String format(LocalDate date)
    {
        return dateFormatter.format(date);
    }

    public static String format(LocalTime time)
    {
        return timeFormatter.format(time);
    }

    public static String format(LocalDateTime datetime)
    {
        return datetimeFormatter.format(datetime);
    }

    //Parse
    //========================================================================

    public static LocalDate parseDate(String dateStr)
    {
        return LocalDate.parse(dateStr, dateFormatter);
    }

    public static LocalTime parseTime(String timeStr)
    {
        return LocalTime.parse(timeStr, timeFormatter);
    }

    public static LocalDateTime parseDatetime(String datetimeStr)
    {
        return LocalDateTime.parse(datetimeStr, datetimeFormatter);
    }
}
