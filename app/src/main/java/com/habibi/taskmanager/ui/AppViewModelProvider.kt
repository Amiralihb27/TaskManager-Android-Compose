package com.habibi.taskmanager.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.habibi.taskmanager.TaskManagerApplication
import com.habibi.taskmanager.ui.EditTask.EditTaskViewModel
import com.habibi.taskmanager.ui.categories.CategoriesViewModel
import com.habibi.taskmanager.ui.more.MoreViewModel
import com.habibi.taskmanager.ui.search.SearchViewModel
import com.habibi.taskmanager.ui.task.TasksViewModel


object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for HomeViewModel
        initializer {
            TasksViewModel(
                this.taskManagerApplication().container.tasksRepository,
                this.taskManagerApplication().container.categoriesRepository,
                this.taskManagerApplication().alarmScheduler
            )
        }

        initializer {
            EditTaskViewModel(
                this.taskManagerApplication().container.tasksRepository,
                this.taskManagerApplication().container.categoriesRepository,
                this.taskManagerApplication().alarmScheduler
            )
        }

        // Initializer for CategoriesViewModel
        initializer {
            CategoriesViewModel(
                this.taskManagerApplication().container.categoriesRepository
            )
        }

        // Initializer for SearchViewModel
        initializer {
            SearchViewModel(
                this.taskManagerApplication().container.tasksRepository,
                this.taskManagerApplication().container.categoriesRepository
            )
        }

        // Initializer for MoreViewModel
        initializer {
            MoreViewModel()
        }

        //

    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [TaskManagerApplication].
 */
fun CreationExtras.taskManagerApplication(): TaskManagerApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as TaskManagerApplication)