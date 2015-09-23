package com.rg.ireminders.utils;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by rustamgaifullin on 4/13/15.
 */
public final class DateUtils {
  public static String getDueDate(Long milliseconds) {
    Date date = new Date(milliseconds);
    DateFormat dateFormat = DateFormat.getInstance();
    String strDate = dateFormat.format(date);

    return strDate;
  }
}
