package com.habibi.taskmanager.data.repository

import com.habibi.taskmanager.data.entities.Category
import com.habibi.taskmanager.data.relations.CategoryWithTasks
import kotlinx.coroutines.flow.Flow

interface CategoriesRepository {

    fun getAllCategories(): Flow<List<Category>>

    fun getCategory(id: Int): Flow<Category>

    fun getCategoryWithTasks(id: Int): Flow<CategoryWithTasks>

    suspend fun getTaskCountForCategory(categoryId: Int): Int

    suspend fun insertCategory(category: Category)

    suspend fun updateCategory(category: Category)

    suspend fun deleteCategory(category: Category)
}