package com.edwin.android.cinerd.util;

import java.util.Calendar;
import java.util.Date;

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
}
