package com.habibi.taskmanager.ui.EditTask

import com.habibi.taskmanager.ui.categories.CategoryDetails
import com.habibi.taskmanager.ui.task.TasksDetails


data class EditTaskUiState(
    val taskDetails: TasksDetails? = null,
    val isLoading: Boolean = true,
    val isTaskSaved: Boolean = false,
    val categoryList : List<CategoryDetails> = emptyList()
)
