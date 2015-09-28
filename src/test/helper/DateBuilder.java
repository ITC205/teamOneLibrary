package test.helper;

import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

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

}
