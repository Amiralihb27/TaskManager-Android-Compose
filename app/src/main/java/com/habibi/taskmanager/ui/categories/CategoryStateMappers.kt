package com.habibi.taskmanager.ui.categories

import com.habibi.taskmanager.data.entities.Category


data class CategoryDetails(
    val categoryId: Int = 0,
    val name: String = "",
    val color: String = "",
    val isEntryValid: Boolean = false
)

data class CategoriesScreenUiState(
    val categoryList: List<CategoryDetails> = emptyList(),
    var isSheetShown: Boolean = false,

    var categoryDetails: CategoryDetails = CategoryDetails(),

    val colorOptions: List<String> = listOf(
        "#FF5733", "#33FF57", "#3357FF", "#FF33A1",
        "#A133FF", "#33FFA1", "#FFC300", "#C70039"
    )
)

fun CategoryDetails.toCategory(): Category = Category(
    categoryId = categoryId ,
    name = name,
    color = color
)

fun Category.toCategoryDetails(): CategoryDetails = CategoryDetails(
    categoryId = categoryId,
    name = name,
    color = color
)