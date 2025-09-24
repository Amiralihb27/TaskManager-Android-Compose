package com.habibi.taskmanager.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.com.habibi.ui.search.SearchScreenUiState
import com.habibi.taskmanager.data.repository.CategoriesRepository
import com.habibi.taskmanager.data.repository.TasksRepository
import com.habibi.taskmanager.ui.categories.toCategoryDetails
import com.habibi.taskmanager.ui.task.toTaskDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class SearchViewModel(
    private val tasksRepository: TasksRepository,
    private val categoriesRepository: CategoriesRepository
) : ViewModel() {
    //

    private val _uiState = MutableStateFlow(SearchScreenUiState())
    val uiState: StateFlow<SearchScreenUiState> = _uiState.asStateFlow()

    init {
        val pendingTasksFlow = tasksRepository.getAllPendingTasks()
        val completedTasksFlow = tasksRepository.getAllCompletedTasks()
        combine(pendingTasksFlow, completedTasksFlow) { pendingTasks, completedTasks ->
            val allTasks = (pendingTasks + completedTasks).map { it -> it.toTaskDetails() }
            _uiState.update { it.copy(tasksList = allTasks) }
        }.launchIn(viewModelScope)
        categoriesRepository.getAllCategories()
            .onEach { categories -> _uiState.update { it.copy(categories = categories.map { category -> category.toCategoryDetails() }) } }
            .launchIn(viewModelScope)

    }

    fun onFilterByTitle(enteredTitle: String?) {
        if (enteredTitle != null) {
            _uiState.update {
                it.copy(
                    searchedTitle = enteredTitle,
                )
            }
        }
    }


}