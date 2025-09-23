package com.habibi.taskmanager.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.habibi.taskmanager.data.entities.Category
import com.habibi.taskmanager.data.entities.Task
import com.habibi.taskmanager.data.dao.CategoryDao
import com.habibi.taskmanager.data.dao.TaskDao

/**
 * Database class with a singleton Instance object.
 */
@Database(
    entities = [Task::class, Category::class], version = 1, exportSchema = false
)
@TypeConverters(Converters::class) // TypeConverter for the TaskStatus enum
abstract class TaskManagerDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao
    abstract fun categoryDao(): CategoryDao

    companion object {
        @Volatile
        private var Instance: TaskManagerDatabase? = null

        fun getDatabase(context: Context): TaskManagerDatabase {
            // if the Instance is not null, return it; otherwise, create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    TaskManagerDatabase::class.java,
                    "task_manager_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
