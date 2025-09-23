package com.habibi.taskmanager.data.repository

import com.habibi.taskmanager.data.dao.TaskDao
import com.habibi.taskmanager.data.entities.Task
import com.habibi.taskmanager.data.repository.TasksRepository
import kotlinx.coroutines.flow.Flow

class OfflineTasksRepository(private val taskDao: TaskDao) : TasksRepository {
    override fun getAllCompletedTasks(): Flow<List<Task>> = taskDao.getAllDoneTasks()

    override fun getTask(id: Int): Flow<Task?> =taskDao.getTask(id)

    override fun searchTasks(searchQuery: String): Flow<List<Task>> = taskDao.searchTasks(searchQuery)

    override fun getAllPendingTasks(): Flow<List<Task>> =taskDao.getAllPendingTasks()

    override fun getTasksByCategory(categoryId: Int): Flow<List<Task>> = taskDao.getTaskByCategory(categoryId)

    override suspend fun insertTask(task: Task) = taskDao.insert(task)

    override suspend fun updateTask(task: Task)  = taskDao.update(task)

    override suspend fun deleteTask(task: Task)  = taskDao.delete(task)
}