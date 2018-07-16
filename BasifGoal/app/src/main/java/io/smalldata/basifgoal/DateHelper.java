package io.smalldata.basifgoal;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

/**
 * Helper.java
 * Created: 1/24/17
 * author: Fabian Okeke
 */

public class DateHelper {

    public static String getTodayDateStr() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }

    public static Date getDatetimeGMT(String datetimeStr) {
        String format = "yyyy-MM-dd'T'HH:mm:ss-05:00";
        return getDatetimeGMT(datetimeStr, format);
    }

    public static Date getDatetimeGMT(String datetimeStr, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Constants.LOCALE);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        Date result = null;
        try {
            result = dateFormat.parse(datetimeStr);
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
        return result;
    }

    public static String getFormattedTimestamp() {
        return new SimpleDateFormat("yyyy-MM-dd h:mm:ss a", Constants.LOCALE).format(System.currentTimeMillis());
    }

    public static String millisToDateFormat(long timeInMillis) {
        if (timeInMillis <= 0) return "Zero.am";
        return new SimpleDateFormat("yyyy-MM-dd h:mm:ss a", Constants.LOCALE).format(timeInMillis);
    }

    public static String dateToStr(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Constants.LOCALE);
        return sdf.format(date);
    }

    public static Date strToDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Constants.LOCALE);
        Date formattedDate = null;
        try {
            formattedDate = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedDate;
    }

    public static Date addDays(Date date, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }

    public boolean isEqualOrGreater(String mainDateStr, String compareDateStr) {
        Date mainDate = strToDate(mainDateStr);
        Date compareDate = strToDate(compareDateStr);
        return mainDate.getTime() >= compareDate.getTime();
    }

    public static int getRandomInt(int min, int max) {
        Random random = new Random();
        int range = max - min + 1;
        return random.nextInt(range) + min;
    }

}


