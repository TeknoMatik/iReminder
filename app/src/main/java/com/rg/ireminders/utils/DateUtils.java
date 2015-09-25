package com.rg.ireminders.utils;

import java.text.DateFormat;
import java.util.Date;

/**
 * Date utils
 */
public final class DateUtils {
  public static String getDueDate(Long milliseconds) {
    Date date = new Date(milliseconds);
    DateFormat dateFormat = DateFormat.getInstance();

    return dateFormat.format(date);
  }
}
