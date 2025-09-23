package com.habibi.taskmanager.data.repository

import com.habibi.taskmanager.data.entities.Task
import kotlinx.coroutines.flow.Flow

interface TasksRepository {

    fun getAllPendingTasks(): Flow<List<Task>>

    fun getAllCompletedTasks(): Flow<List<Task>>

    fun getTask(id: Int): Flow<Task?>

    fun searchTasks(searchQuery: String): Flow<List<Task>>

    fun getTasksByCategory(categoryId: Int): Flow<List<Task>>

    suspend fun insertTask(task: Task)

    suspend fun updateTask(task: Task)

    suspend fun deleteTask(task: Task)
}