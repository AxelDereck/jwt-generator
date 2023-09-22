package org.beenoo.tool;

import java.util.Calendar;
import java.util.Date;

public class DateTools {
    private static final Calendar calendar = Calendar.getInstance();

    public static Date addMinutes(Date initial, int minutes) {
        calendar.setTime(initial);
        calendar.add(Calendar.MINUTE, minutes);
        return calendar.getTime();
    }
}
