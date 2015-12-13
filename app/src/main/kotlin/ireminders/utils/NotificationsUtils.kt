package com.rg.ireminders.utils

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.net.Uri
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.rg.ireminders.R

/**
 * Utility class for notifications
 */
object NotificationsUtils {
    private val TAG = "NotificationsUtils"

    fun showNotification(context: Context, title: String, taskUri: Uri, notificationId: Int) {
        Log.d(TAG, "Showing notification with title: " + title)
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = NotificationCompat.Builder(context)
        builder.setSmallIcon(R.drawable.ic_notification)
        builder.setContentTitle(context.getString(R.string.notif_tittle))
        builder.setContentText(title)
        builder.setTicker(title)
        builder.setAutoCancel(true)
        builder.setDefaults(Notification.DEFAULT_ALL)
        builder.setPriority(NotificationCompat.PRIORITY_HIGH)
        builder.setCategory(NotificationCompat.CATEGORY_EVENT)
        notificationManager.notify(notificationId, builder.build())
    }
}
