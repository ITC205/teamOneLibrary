package test.helper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import library.interfaces.entities.ILoan;

/**
 * Provides static helper methods for building dates via a simpler interface
 * that allows the user to specify the day of month, the month and the year.
 *
 * @author nicholasbaldwin
 */
public class DateBuilder
{
  /**
   * Create Date using simple interface (via Calendar) where time portion is
   * set to zero.
   * @param day   int The day of the month (1 or 2 digits).
   * @param month int The month of the year (1 or 2 digits, where 0 = January,
   *              11 = December).
   * @param year  int The year (4 digits).
   * @return Date Date with time portion set to 0).
   */
  public static java.util.Date dateBuilder(int day, int month, int year)
  {
    Calendar calendar = new GregorianCalendar();
    calendar.set(year, month, day, 0, 0, 0);
    Date date = calendar.getTime();
    return date;
  }



  /**
   * Create Date using simple interface (via Calendar) including time portion.
   * @param day   int The day of the month (1 or 2 digits).
   * @param month int The month of the year (1 or 2 digits, where 0 = January,
   *              11 = December).
   * @param year  int The year (4 digits).
   * @param hour  int The hour of the day (1 or 2 digits, using 24 hour clock).
   * @param min   int The minutes of the hour (1 or 2 digits).
   * @param sec   int The seconds of the minute (1 or 2 digits).
   * @return Date Date with time portion set to 0).
   */
  public static Date dateBuilder(int day, int month, int year,
                                 int hour, int min, int sec)
  {
    Calendar calendar = new GregorianCalendar();
    calendar.set(year, month, day, hour, min, sec);
    Date date = calendar.getTime();
    return date;
  }



  /**
   * Create Date using simple interface (via Calendar) where time portion is
   * set to zero.
   * @param date Date The reference date.
   * @param offsetDays int The number of days to be added or subtracted to the
   *                   reference date.
   * @return Date Date with offsetDays added or subtracted to reference date.
   */
  public static Date dateBuilder(Date date, int offsetDays)
  {
    Calendar calendar = new GregorianCalendar();
    calendar.setTime(date);
    calendar.add(Calendar.DAY_OF_MONTH, offsetDays);
    return calendar.getTime();
  }



  public static String formattedDate(Date date)
  {
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    String dateString = dateFormat.format(date);
    return dateString;
  }



  public static Date ignoreTime(Date date)
  {
    Calendar calendar = Calendar.getInstance();

    calendar.setTime(date);
    calendar.set(java.util.Calendar.HOUR_OF_DAY, 0);
    calendar.set(java.util.Calendar.MINUTE, 0);
    calendar.set(java.util.Calendar.SECOND, 0);
    calendar.set(java.util.Calendar.MILLISECOND, 0);

    return calendar.getTime();
  }



  public static Date calculateDueDate(Date date)
  {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.add(Calendar.DAY_OF_MONTH, ILoan.LOAN_PERIOD);
    return calendar.getTime();
  }

}
