package com.habibi.taskmanager.ui.task


import com.habibi.taskmanager.data.entities.Task
import com.habibi.taskmanager.data.entities.TaskStatus
import com.habibi.taskmanager.ui.categories.CategoryDetails


data class TasksDetails(
    val taskId: Int = 0,
    val title: String = "",
    val description: String = "",
    val status: TaskStatus = TaskStatus.PENDING,
    val categoryId: Int = 0,
    val dueDate: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val isEntryValid: Boolean = false
)

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
