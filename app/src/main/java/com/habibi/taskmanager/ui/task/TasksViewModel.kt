package com.habibi.taskmanager.ui.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.habibi.taskmanager.data.entities.TaskStatus
import com.habibi.taskmanager.data.repository.CategoriesRepository
import com.habibi.taskmanager.data.repository.TasksRepository
import com.habibi.taskmanager.notifications.scheduler.AndroidAlarmScheduler
import com.habibi.taskmanager.ui.categories.toCategoryDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TasksViewModel(
    private val tasksRepository: TasksRepository,
    private val categoriesRepository: CategoriesRepository,
    private val alarmScheduler: AndroidAlarmScheduler
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(TasksScreenUiState())
    val uiState: StateFlow<TasksScreenUiState> = _uiState.asStateFlow()

    init {
        tasksRepository.getAllPendingTasks()
            .onEach { tasks ->
                _uiState.update { it.copy(tasksList = tasks.map { task -> task.toTaskDetails() }) }
            }
            .launchIn(viewModelScope)

        categoriesRepository.getAllCategories()
            .onEach { categories -> _uiState.update { it.copy(categories = categories.map { category -> category.toCategoryDetails() }) } }
            .launchIn(viewModelScope)

    }

    fun onFilterByCategory(categoryId: Int?){
        _uiState.update {
            it.copy(
                selectedFilterCategoryId = categoryId
            )
        }
    }

    fun updateTaskDetails(newDetails: TasksDetails) {
        _uiState.update {
            it.copy(
                tasksDetails = newDetails.copy(
                    isEntryValid = validateInput(newDetails)
                )
            )
        }

    }

    private fun validateTextInput(details: TasksDetails = _uiState.value.tasksDetails): Boolean {
        // A Task is valid if its title is not blank
        return details.title.isNotBlank()
    }

    private fun validateInput(details: TasksDetails = _uiState.value.tasksDetails): Boolean {
        // A Task is valid if its title is not blank and has a category.
        return details.title.isNotBlank() && details.categoryId != 0
    }

    fun onAddTaskDetails() {
        _uiState.update {
            it.copy(
                isSheetShown = true,
                tasksDetails = TasksDetails(isEntryValid = false)
            )
        }
    }

    fun onEditTaskDetails(task: TasksDetails) {
        _uiState.update {
            it.copy(
                isSheetShown = true,
                tasksDetails = TasksDetails(isEntryValid = true)
            )
        }
    }

    fun onSheetDismiss() {
        _uiState.update {
            it.copy(isSheetShown = false)
        }
    }

    fun onSaveTask() {
        if (_uiState.value.tasksDetails.isEntryValid) {
            viewModelScope.launch {
                val taskToSave = _uiState.value.tasksDetails.toTasks()
                if (taskToSave.categoryId != 0 && taskToSave.taskId == 0) {
                    tasksRepository.insertTask(taskToSave)
                } else {
                    tasksRepository.updateTask(uiState.value.tasksDetails.toTasks())
                }
            }
        }
        onSheetDismiss()
    }

    fun onDoneTask(task: TasksDetails) {
        viewModelScope.launch {
            val updatedTask = task.copy(status = TaskStatus.DONE)
            tasksRepository.updateTask(updatedTask.toTasks())
             alarmScheduler.cancel(updatedTask)
        }
    }
    fun onPendingTask(task: TasksDetails){
        viewModelScope.launch{
            val updatedTask = task.copy(status = TaskStatus.PENDING)
            tasksRepository.updateTask(updatedTask.toTasks())
        }
    }


}