package com.habibi.taskmanager.data.repository

import com.habibi.taskmanager.data.dao.CategoryDao
import com.habibi.taskmanager.data.entities.Category
import com.habibi.taskmanager.data.relations.CategoryWithTasks
import com.habibi.taskmanager.data.repository.CategoriesRepository
import kotlinx.coroutines.flow.Flow

class OfflineCategoriesRepository(private  val categoryDao: CategoryDao): CategoriesRepository {
    override fun getAllCategories(): Flow<List<Category>> = categoryDao.getAllCategories()

    override fun getCategory(id: Int): Flow<Category> = categoryDao.getCategory(id)

    override fun getCategoryWithTasks(id: Int): Flow<CategoryWithTasks> = categoryDao.getCategoryWithTasks(id)

    override suspend fun getTaskCountForCategory(categoryId: Int): Int = categoryDao.getTaskCountForCategory(categoryId)

    override suspend fun insertCategory(category: Category) = categoryDao.insert(category)

    override suspend fun updateCategory(category: Category) = categoryDao.update(category)

    override suspend fun deleteCategory(category: Category) = categoryDao.delete(category)


}