package com.habibi.taskmanager.data.repository

import androidx.compose.runtime.mutableStateOf
import com.habibi.taskmanager.data.entities.Task
import com.habibi.taskmanager.data.entities.TaskStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map


class FakeTasksRepository(initilaTasks:List<Task> = emptyList()): TasksRepository {


    private val tasks = initilaTasks.toMutableList()
    private val tasksFlow = MutableStateFlow(tasks)

    val logged = mutableListOf<String>()
    override fun getAllPendingTasks(): Flow<List<Task>> {
    return tasksFlow.asStateFlow().map { list-> list.filter { it.status == TaskStatus.PENDING } }
    }


    override fun getAllCompletedTasks(): Flow<List<Task>> {
        TODO("Not yet implemented")
    }

    override fun getTask(id: Int): Flow<Task?> {
        TODO("Not yet implemented")
    }

    override fun searchTasks(searchQuery: String): Flow<List<Task>> {
        TODO("Not yet implemented")
    }

    override fun getTasksByCategory(categoryId: Int): Flow<List<Task>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertTask(task: Task) {
        logged.add("insertTask: ${task.title}")
        tasks.add(task)
        tasksFlow.value = tasks.toMutableList()
    }

    override suspend fun updateTask(task: Task) {
        logged.add("updateTask: ${task.title} to ${task.status}")
        val index = tasks.indexOfFirst { it.taskId == task.taskId }
        if (index != -1) {
            tasks[index] = task
        }
        tasksFlow.value = tasks.toMutableList()
    }

    override suspend fun deleteTask(task: Task) {
        TODO("Not yet implemented")
    }
}