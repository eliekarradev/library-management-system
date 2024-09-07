package libraryMS.utils.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

public class DateUtils {

    private static Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
    private static SimpleDateFormat yyyyMMFormat = new SimpleDateFormat("yyyyMM");;


    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date1.getTime() - date2.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    public static Integer getYearOf(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    public static Integer getHourFromDate(Date date) {
        calendar.setTime(date);   // assigns calendar to given date
        return calendar.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format
    }

    public static Integer getMinuteFromDate(Date date) {
        calendar.setTime(date);   // assigns calendar to given date
        return calendar.get(Calendar.MINUTE); // gets hour in 24h format
    }

    public static String getHourAndMinuteFromDate(Date date) {
        calendar.setTime(date);
        String hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        String minute = String.valueOf(calendar.get(Calendar.MINUTE));
        return hour + ":" + minute;
    }

    public static String getYearMonthFromCurrentDate(){
       return yyyyMMFormat.format(Calendar.getInstance().getTime());
    }

    public static Date getDateAfterMonthsAdded(Date date,Integer months){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, months);
        return c.getTime();
    }

    public static Long getStartDayOfDate(Long date){
        Calendar cal = Calendar.getInstance();       // get calendar instance
        cal.setTimeInMillis(date);                           // set cal to date
        cal.set(Calendar.HOUR_OF_DAY, 0);            // set hour to midnight
        cal.set(Calendar.MINUTE, 0);                 // set minute in hour
        cal.set(Calendar.SECOND, 0);                 // set second in minute
        cal.set(Calendar.MILLISECOND, 0);            // set millis in second
        return cal.getTimeInMillis();
    }

    public static Long getEndDayOfDate(Long date){
        Calendar cal = Calendar.getInstance();       // get calendar instance
        cal.setTimeInMillis(date);                           // set cal to date
        cal.set(Calendar.HOUR_OF_DAY, 23);            // set hour to midnight
        cal.set(Calendar.MINUTE, 59);                 // set minute in hour
        cal.set(Calendar.SECOND, 59);                 // set second in minute
        cal.set(Calendar.MILLISECOND, 0);            // set millis in second
        return cal.getTimeInMillis();
    }
}
