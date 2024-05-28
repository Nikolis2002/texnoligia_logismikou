package com.ceid.util;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

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

    public static LocalDateTime parseFromJS(String datetimeStr)
    {
        Instant instant = Instant.parse(datetimeStr);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    //Other
    //========================================================================

    public static LocalDate toLocalDate(Date date)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        return LocalDate.parse(dateFormat.format(date), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

    public static String dayOfWeekShort(Date date)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        LocalDate localDate = LocalDate.parse(dateFormat.format(date), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String day = localDate.getDayOfWeek().name().substring(0, 3);
        day = day.charAt(0) + day.substring(1).toLowerCase();

        return day;
    }

    public static int dayOfWeekNum(Date date)
    {
        String day = dayOfWeekShort(date);

        java.util.Map<String, Integer> dayMap = new HashMap<String, Integer>();

        dayMap.put("Mon", 0);
        dayMap.put("Tue", 1);
        dayMap.put("Wed", 2);
        dayMap.put("Thu", 3);
        dayMap.put("Fri", 4);
        dayMap.put("Sat", 5);
        dayMap.put("Sun", 6);

        return dayMap.get(day);
    }

    public static int dayOfWeekNum(String day)
    {
        java.util.Map<String, Integer> dayMap = new HashMap<String, Integer>();

        dayMap.put("Mon", 0);
        dayMap.put("Tue", 1);
        dayMap.put("Wed", 2);
        dayMap.put("Thu", 3);
        dayMap.put("Fri", 4);
        dayMap.put("Sat", 5);
        dayMap.put("Sun", 6);

        return dayMap.get(day);
    }

    public static String millisToTimeString(long millis)
    {
        int minutes = (int) ((millis/1000)/60);
        int seconds = (int) ((millis/1000)%60);
        return String.format("%02d:%02d", minutes, seconds);
    }
}
