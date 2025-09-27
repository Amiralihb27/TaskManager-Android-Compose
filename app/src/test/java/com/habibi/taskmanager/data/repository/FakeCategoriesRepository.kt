package com.habibi.taskmanager.data.repository

import com.habibi.taskmanager.data.entities.Category
import com.habibi.taskmanager.data.relations.CategoryWithTasks
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeCategoriesRepository(initialCategories : List<Category> = emptyList()) : CategoriesRepository {

    private val categories = initialCategories.toMutableList()
    private val categoriesFlow = MutableStateFlow(categories)

    val logged = mutableListOf<String>()

    override fun getAllCategories(): Flow<List<Category>> {
       return categoriesFlow.asStateFlow()
    }

    override suspend fun insertCategory(category: Category) {
        logged.add("insertCategory: ${category.name}")
        categories.add(category)
        categoriesFlow.value = categories.toMutableList()
    }

    override fun getCategory(id: Int): Flow<Category> {
        TODO("Not yet implemented")
    }

    override fun getCategoryWithTasks(id: Int): Flow<CategoryWithTasks> {
        TODO("Not yet implemented")
    }

    override suspend fun getTaskCountForCategory(categoryId: Int): Int {
        TODO("Not yet implemented")
    }

    override suspend fun updateCategory(category: Category) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCategory(category: Category) {
        logged.add("deleteCategory: ${category.name}")
        categories.removeIf{it.categoryId == category.categoryId}
        categoriesFlow.value = categories.toMutableList()
    }
}