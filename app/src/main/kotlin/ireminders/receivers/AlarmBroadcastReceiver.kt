package com.rg.ireminders.receivers

import android.content.BroadcastReceiver
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.rg.ireminders.utils.NotificationsUtils
import org.dmfs.provider.tasks.broadcast.DueAlarmBroadcastHandler

/**
 * Alarm broadcast receiver
 */
class AlarmBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == DueAlarmBroadcastHandler.BROADCAST_DUE_ALARM) {
            val taskUri = intent.data
            val title = intent.getStringExtra(DueAlarmBroadcastHandler.EXTRA_TASK_TITLE)
            val notificationId = ContentUris.parseId(taskUri).toInt()

            NotificationsUtils.showNotification(context, title, taskUri, notificationId)
        }
    }
}
