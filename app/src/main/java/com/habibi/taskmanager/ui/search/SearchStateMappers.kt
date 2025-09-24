package com.com.habibi.ui.search

import com.habibi.taskmanager.ui.categories.CategoryDetails
import com.habibi.taskmanager.ui.task.TasksDetails


data class SearchScreenUiState(
    val tasksList: List<TasksDetails> = emptyList(),
//    var isSheetShown: Boolean = false,
    val categories: List<CategoryDetails> = emptyList(),
//    val tasksDetails: TasksDetails = TasksDetails(),
    val searchedTitle: String? = null
) {
    val filteredTaskList: List<TasksDetails> = if (searchedTitle.isNullOrBlank()) {
        tasksList
    } else {
        tasksList.filter { task ->
            task.title.contains(searchedTitle, ignoreCase = true)
        }
    }
}

