package com.habibi.taskmanager.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tasks",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["categoryId"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("categoryId")]
)
data class Task(
    @PrimaryKey(autoGenerate = true)
    val taskId: Int = 0,
    val title: String,
    val description: String,
    val status: TaskStatus = TaskStatus.PENDING,
    val categoryId: Int,
    val dueDate: Long? = null, // Nullable so a task can be open-ended
    val createdAt: Long = currentTimestamp(),
    val updatedAt: Long = currentTimestamp()
)

enum class TaskStatus {
    PENDING, DONE
}

// Get current UTC time in milliseconds
//@SuppressLint("NewApi")
//fun currentTimestamp(): Long {
//    return LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()
//}

fun currentTimestamp(): Long {
    // Use the older, compatible API
    return System.currentTimeMillis()
}
