package com.habibi.taskmanager.ui.task


import com.habibi.taskmanager.data.entities.Task
import com.habibi.taskmanager.data.entities.TaskStatus
import com.habibi.taskmanager.ui.categories.CategoryDetails
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit


data class TasksDetails(
    val taskId: Int = 0,
    val title: String = "",
    val description: String = "",
    val status: TaskStatus = TaskStatus.PENDING,
    val categoryId: Int = 0,
    val dueDate: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val isEntryValid: Boolean = false
) {
    val timeReminder: String
        get() {
            val selectedMillis = dueDate ?: return ""
            val now = Calendar.getInstance()
            val givenTime = Calendar.getInstance().apply { timeInMillis = selectedMillis }
            val diffMillis = givenTime.timeInMillis - now.timeInMillis

            val sameYear = now.get(Calendar.YEAR) == givenTime.get(Calendar.YEAR)
            val sameDay =
                sameYear && now.get(Calendar.DAY_OF_YEAR) == givenTime.get(Calendar.DAY_OF_YEAR)
            val timeFormat = SimpleDateFormat("yyyy-MM-dd , HH:mm", Locale.getDefault())
            if (sameDay) {
                val hours = TimeUnit.MILLISECONDS.toHours(diffMillis)
                val hourValue = Math.abs(hours)
                val suffix = if (diffMillis < 0) "ago" else "from now"

                val minutes = TimeUnit.MILLISECONDS.toMinutes(diffMillis) % 60
                val minutesValue = Math.abs(minutes)
                val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                val formattedTime = timeFormat.format(givenTime.time)


                return "$hourValue hours $minutesValue minutes $suffix, $formattedTime"
            } else if (sameYear) {
                val timeFormat = SimpleDateFormat("d MMM, HH:mm", Locale.getDefault())
                return "${timeFormat.format(givenTime.time)}"
            } else {
                return "${timeFormat.format(givenTime.time)}"
            }
        }
}

data class TasksScreenUiState(
    val tasksList: List<TasksDetails> = emptyList(),
    var isSheetShown: Boolean = false,
    val categories: List<CategoryDetails> = emptyList(),
    var tasksDetails: TasksDetails = TasksDetails(),
    var selectedFilterCategoryId: Int? = null
) {
    val filteredTaskList: List<TasksDetails> = if (selectedFilterCategoryId == null) {
        tasksList
    } else {
        tasksList.filter { it.categoryId == selectedFilterCategoryId }
    }
}

fun TasksDetails.toTasks(): Task = Task(
    taskId = taskId,
    title = title,
    description = description,
    status = status,
    dueDate = dueDate,
    categoryId = categoryId,
    createdAt = createdAt
)


fun Task.toTaskDetails(): TasksDetails = TasksDetails(
    taskId = taskId,
    title = title,
    description = description,
    status = status,
    dueDate = dueDate,
    categoryId = categoryId,
    createdAt = createdAt
)
