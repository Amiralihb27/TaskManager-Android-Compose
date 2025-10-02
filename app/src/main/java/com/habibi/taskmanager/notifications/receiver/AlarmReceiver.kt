package com.habibi.taskmanager.notifications.receiver

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.habibi.taskmanager.MainActivity
import com.habibi.taskmanager.TaskManagerApplication
import com.habibi.taskmanager.data.entities.TaskStatus

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val taskId = intent.getIntExtra("TASK_ID", -1)
        val title = intent.getStringExtra("TASK_TITLE") ?: "Task Reminder"
        val status = intent.getStringExtra("TASK_STATUS")

        val notificationId = taskId
        if (notificationId == -1) {
            Log.e("AlarmReceiver", "Received alarm with invalid TASK_ID")
            return
        }

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (status == TaskStatus.PENDING.name) {
            val activityIntent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }

            val activityPendingIntent = PendingIntent.getActivity(
                context,
                notificationId,
                activityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val notification =
                NotificationCompat.Builder(context, TaskManagerApplication.CHANNEL_ID)
                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                    .setContentTitle(title)
                    .setContentText("Time to complete the task!")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .setContentIntent(activityPendingIntent)
                    .build()

            notificationManager.notify(notificationId, notification)

        } else {
            Log.d(
                "AlarmReceiver",
                "$status"
            )
            notificationManager.cancel(notificationId)
        }
    }
}