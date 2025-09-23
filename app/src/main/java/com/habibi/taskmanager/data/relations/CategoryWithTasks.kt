package com.habibi.taskmanager.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.habibi.taskmanager.data.entities.Category
import com.habibi.taskmanager.data.entities.Task

data class CategoryWithTasks(
    @Embedded val category: Category,
    @Relation(
        parentColumn = "categoryId",// The primary key of the Category entity
        entityColumn = "categoryId"// The foreign key in the Task entity
    )
    val tasks: List<Task>
)