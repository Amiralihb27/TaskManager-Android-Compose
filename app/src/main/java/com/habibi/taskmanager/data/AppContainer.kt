package com.habibi.taskmanager.data

import android.content.Context
import com.habibi.taskmanager.data.db.TaskManagerDatabase
import com.habibi.taskmanager.data.repository.CategoriesRepository
import com.habibi.taskmanager.data.repository.OfflineCategoriesRepository
import com.habibi.taskmanager.data.repository.OfflineTasksRepository
import com.habibi.taskmanager.data.repository.TasksRepository


/**
 * App container for Dependency injection.
 */

interface  AppContainer{
    val tasksRepository : TasksRepository
    val categoriesRepository : CategoriesRepository
}

class AppDataContainer(private val context: Context) : AppContainer {


    override val tasksRepository: TasksRepository by lazy {
        OfflineTasksRepository(TaskManagerDatabase.getDatabase(context).taskDao())
    }

    override val categoriesRepository: CategoriesRepository by lazy {
        OfflineCategoriesRepository(TaskManagerDatabase.getDatabase(context).categoryDao())
    }
}