package com.habibi.taskmanager.notifications.scheduler

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.habibi.taskmanager.notifications.receiver.AlarmReceiver
import com.habibi.taskmanager.ui.task.TasksDetails
import java.time.ZoneId

class AndroidAlarmScheduler(
    private val context: Context
) : AlarmScheduler {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override fun schedule(task: TasksDetails) {
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra("TASK_ID", task.taskId)
        intent.putExtra("TASK_TITLE", task.title)
        intent.putExtra("TASK_DUE_DATE", task.dueDate)
        intent.putExtra("TASK_STATUS",task.status.name)

        task.dueDate?.let {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                task.dueDate,
                PendingIntent.getBroadcast(
                    context,
                    task.taskId,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )
        }
    }

    override fun cancel(task: TasksDetails) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                task.taskId,
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}