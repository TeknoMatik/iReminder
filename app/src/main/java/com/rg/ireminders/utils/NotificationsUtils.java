package com.rg.ireminders.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.rg.ireminders.R;

/**
 * Utility class for notifications
 */
public final class NotificationsUtils {
  private static final String TAG = "NotificationsUtils";

  public static void showNotification(Context context, String title, Uri taskUri, int notificationId) {
    Log.d(TAG, "Showing notification with title: " + title);
    NotificationManager notificationManager =
        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
    builder.setSmallIcon(R.drawable.ic_notification);
    builder.setContentTitle(context.getString(R.string.notif_tittle));
    builder.setContentText(title);
    builder.setTicker(title);
    builder.setAutoCancel(true);
    builder.setDefaults(Notification.DEFAULT_ALL);
    builder.setPriority(NotificationCompat.PRIORITY_HIGH);
    builder.setCategory(NotificationCompat.CATEGORY_EVENT);
    notificationManager.notify(notificationId, builder.build());
  }
}
