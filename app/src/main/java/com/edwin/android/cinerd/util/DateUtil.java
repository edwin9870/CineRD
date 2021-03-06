package com.edwin.android.cinerd.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by Edwin Ramirez Ventura on 7/21/2017.
 */

public final class DateUtil {

    public static Date addDay(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    public static String formatDateTime(Date date) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate
        // UTC, no timezone offset
        df.setTimeZone(tz);

        return df.format(date);
    }

    public static long daysBetweenDates(Date firstDate, Date secondDate) {
        long firstDateDays = TimeUnit.MILLISECONDS.toDays(firstDate.getTime());
        long secondDateDays = TimeUnit.MILLISECONDS.toDays(secondDate.getTime());
        return Math.abs(firstDateDays-secondDateDays);
    }

    public static Date getDateFromString(String date) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        dateFormat.setTimeZone(tz);
        Date convertedDate;
        try {
            convertedDate = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return convertedDate;
    }

    public static String formatDate(Date date) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd"); // Quoted "Z" to indicate UTC, no
        df.setTimeZone(tz);
        return df.format(date);
    }

    public static boolean areSameDay(Date firstDate, Date secondDate) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(firstDate);
        cal2.setTime(secondDate);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

}
