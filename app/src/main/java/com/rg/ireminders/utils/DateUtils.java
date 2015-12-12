package com.rg.ireminders.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Date utils
 */
public final class DateUtils {
  public static String getDueDateTime(Long milliseconds) {
    Date date = new Date(milliseconds);
    DateFormat dateFormat = DateFormat.getInstance();

    return dateFormat.format(date);
  }

  public static String getDueTime(Long milliseconds) {
    Date date = new Date(milliseconds);
    DateFormat dateFormat = DateFormat.getTimeInstance(DateFormat.SHORT);

    return dateFormat.format(date);
  }

  public static String getDueDate(Long milliseconds) {
    Date date = new Date(milliseconds);
    DateFormat dateFormat = DateFormat.getDateInstance();

    return dateFormat.format(date);
  }

  public static int getDaysCount(Long milliseconds) {
    Calendar todayCalendar = Calendar.getInstance();
    Calendar dueCalendar = Calendar.getInstance();
    dueCalendar.setTimeInMillis(milliseconds);

    if (dueCalendar.getTimeInMillis() >= todayCalendar.getTimeInMillis()) {
      todayCalendar.set(Calendar.HOUR_OF_DAY, 0);
      todayCalendar.set(Calendar.MINUTE, 0);
      todayCalendar.set(Calendar.SECOND, 1);

      dueCalendar.set(Calendar.HOUR_OF_DAY, 23);
      dueCalendar.set(Calendar.MINUTE, 59);
      dueCalendar.set(Calendar.SECOND, 59);
    } else {
      todayCalendar.set(Calendar.HOUR_OF_DAY, 23);
      todayCalendar.set(Calendar.MINUTE, 59);
      todayCalendar.set(Calendar.SECOND, 59);

      dueCalendar.set(Calendar.HOUR_OF_DAY, 0);
      dueCalendar.set(Calendar.MINUTE, 0);
      dueCalendar.set(Calendar.SECOND, 1);
    }

    long endOfDay = todayCalendar.getTimeInMillis();
    long due = dueCalendar.getTimeInMillis();

    return (int) ((due - endOfDay) / (60 * 60 * 24 * 1000));
  }

}
