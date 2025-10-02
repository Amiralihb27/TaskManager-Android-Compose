package com.habibi.taskmanager.ui.EditTask

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.habibi.taskmanager.data.entities.currentTimestamp
import com.habibi.taskmanager.data.repository.CategoriesRepository
import com.habibi.taskmanager.data.repository.TasksRepository
import com.habibi.taskmanager.ui.categories.toCategoryDetails
import com.habibi.taskmanager.ui.task.TasksDetails
import com.habibi.taskmanager.ui.task.toTaskDetails
import com.habibi.taskmanager.ui.task.toTasks
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime


class EditTaskViewModel(
    private val tasksRepository: TasksRepository,
    private val categoriesRepository: CategoriesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditTaskUiState());
    val uiState: StateFlow<EditTaskUiState> = _uiState.asStateFlow()

    init {
        categoriesRepository.getAllCategories()
            .onEach { categories -> _uiState.update { it.copy(categoryList = categories.map { category -> category.toCategoryDetails() }) } }
            .launchIn(viewModelScope)
    }

    fun loadTask(taskId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val selectedTask: TasksDetails? =
                tasksRepository.getTask(taskId).first()?.toTaskDetails()
            _uiState.update {
                it.copy(
                    isLoading = false,
                    taskDetails = selectedTask,

                    )
            }

        }


    }

    private fun validateTextInput(filledText: String): Boolean {
        // A Task is valid if its title is not blank
        return filledText.isNotBlank()
    }

    private fun validateInput(details: TasksDetails): Boolean {
        // A Task is valid if its title is not blank and has a category.
        return details.title.isNotBlank() && details.categoryId != 0
    }

    fun onTitleChange(newTitle: String) {
        _uiState.update {
            //ask the differences between this and below
            it.copy(
                taskDetails = it.taskDetails?.copy(
                    title = newTitle,
                    isEntryValid = validateTextInput(newTitle)
                )
            )
        }
    }

    fun onDescriptionChange(newDescription: String) {
        _uiState.update { currentState ->
            currentState.copy(
                taskDetails = currentState.taskDetails?.copy(
                    description = newDescription,
                    isEntryValid = validateTextInput(newDescription)
                )
            )
        }
    }

//    fun onCategoryChange(newDetails: TasksDetails) {
//        _uiState.update { currentState ->
//            currentState.copy(
//                taskDetails = newDetails.copy(
//                    isEntryValid = validateInput(newDetails)
//                )
//            )
//        }
//    }

    fun onCategoryChange(newCategoryId: Int?) {
        newCategoryId?.let {
            _uiState.update { currentState ->
                val updatedTaskDetails = currentState.taskDetails?.copy(
                    categoryId = newCategoryId
                )
                currentState.copy(
                    taskDetails = updatedTaskDetails?.copy(
                        isEntryValid = validateInput(updatedTaskDetails)
                    )
                )
            }
        }

    }

    fun onDueDateChange(newDueDateMillis: Long?) {
        _uiState.update { currentState ->
            currentState.copy(
                taskDetails = currentState.taskDetails?.copy(
                    dueDate = newDueDateMillis,
                    isEntryValid = true
                )
            )
        }
    }

    fun saveTask() {
        val currentTaskDetails = _uiState.value.taskDetails
        if (currentTaskDetails != null && currentTaskDetails.isEntryValid) {
            viewModelScope.launch {
                tasksRepository.updateTask(currentTaskDetails.toTasks())
                _uiState.update { it.copy(isTaskSaved = true) }
            }
        }

    }
}

