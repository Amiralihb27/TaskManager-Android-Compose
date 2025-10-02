package com.habibi.taskmanager

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.habibi.taskmanager.data.AppContainer
import com.habibi.taskmanager.data.AppDataContainer
import com.habibi.taskmanager.notifications.scheduler.AndroidAlarmScheduler

class TaskManagerApplication : Application() {

    lateinit var container: AppContainer
    lateinit var alarmScheduler: AndroidAlarmScheduler

    companion object {
        const val CHANNEL_ID = "task_reminders_channel"
    }

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
        createNotificationChannel()
        alarmScheduler = AndroidAlarmScheduler(this)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Task Reminders",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.apply { description = "Notifications for task due dates" }
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}