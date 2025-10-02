package com.habibi.taskmanager.notifications.scheduler

import com.habibi.taskmanager.ui.task.TasksDetails


interface AlarmScheduler {
    fun schedule(task: TasksDetails)
    fun cancel(item: TasksDetails)
}