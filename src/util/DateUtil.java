package util;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public final class DateUtil {

    private final static String pattern = "MM/dd/yyyy";
    public static Date getDateFromString(String dateString){

        DateFormat dateFormat =  new SimpleDateFormat(pattern);
        Date date;
        try{
            date = dateFormat.parse(dateString);
//            String newDate = dateFormat.format(date);
            return date;
        }catch (ParseException ignored){

        }

        return  null;
    }

    public static String getStringFromDate(Date today){

        Format formatter = new SimpleDateFormat(pattern);
        return formatter.format(today);
    }

    public static Date addDaysToDate(Date date, int day){

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, day);
        date = c.getTime();
        return date;
    }

    public static int getDayOfWeek(Date date){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DAY_OF_MONTH);
    }
}