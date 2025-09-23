package com.habibi.taskmanager.data.entities
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true)
    val categoryId: Int = 0,
    val name: String,
    val color: String, // Hex color code
    val createdAt: Long = currentTimestamp()
)
