package com.habibi.taskmanager.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.habibi.taskmanager.data.entities.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * From tasks WHERE status == 'PENDING' Order By createdAt desc")
    fun getAllPendingTasks(): Flow<List<Task>>

    @Query("SELECT * From tasks WHERE status == 'DONE' Order By createdAt desc")
    fun getAllDoneTasks(): Flow<List<Task>>

    @Query("SELECT * From tasks  Order By createdAt desc")
    fun getAllTasks(): Flow<List<Task>>

    @Query("SELECT * From tasks where  taskId = :id ")
    fun getTask(id: Int): Flow<Task>


    @Query("SELECT * From tasks where  categoryId = :categoryId ")
    fun getTaskByCategory(categoryId: Int): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE title LIKE '%' || :searchQuery || '%'  ORDER BY createdAt DESC")
    fun searchTasks(searchQuery: String): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

//    @Query("SELECT * From categories ORDER BY name ASC")
//    fun getAllCategories(): Flow<List<Category>>

}
