package com.habibi.taskmanager.ui.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.habibi.taskmanager.data.repository.CategoriesRepository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CategoriesViewModel(
    private val categoriesRepository: CategoriesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoriesScreenUiState())
    val uiState: StateFlow<CategoriesScreenUiState> = _uiState.asStateFlow()

    init {
        categoriesRepository.getAllCategories()
            .onEach { categories ->
                _uiState.update { it.copy(categoryList = categories.map { category -> category.toCategoryDetails() }) }
            }
            .launchIn(viewModelScope)
    }


    fun updateCategoryDetails(newDetails: CategoryDetails) {
        _uiState.update {
            it.copy(
                categoryDetails = newDetails.copy(
                    isEntryValid = validateInput(newDetails)
                )
            )
        }
    }

    private fun validateInput(details: CategoryDetails = _uiState.value.categoryDetails): Boolean {
        // A category is valid if its name is not blank.
        return details.name.isNotBlank()
    }

    fun onAddCategoryClicked() {
        val defaultColor = _uiState.value.colorOptions.first()
        _uiState.update {
            it.copy(
                isSheetShown = true,
                categoryDetails = CategoryDetails(color = defaultColor, isEntryValid = false)
            )
        }
    }

    fun onEditCategoryClicked(category: CategoryDetails) {
        _uiState.update {
            it.copy(
                isSheetShown = true,
                categoryDetails = category.copy(isEntryValid = true)
            )
        }
    }

    fun onSheetDismiss() {
        _uiState.update {
            it.copy(isSheetShown = false)
        }
    }

    fun onDeleteCategory() {
        viewModelScope.launch {
            categoriesRepository.deleteCategory(_uiState.value.categoryDetails.toCategory())
        }
        onSheetDismiss()
    }

    fun onSaveCategory() {

        if (_uiState.value.categoryDetails.isEntryValid) {
            viewModelScope.launch {
                val categoryToSave = _uiState.value.categoryDetails.toCategory()
                if (categoryToSave.categoryId == 0) {
                    categoriesRepository.insertCategory(categoryToSave)
                } else {
                    categoriesRepository.updateCategory(categoryToSave)
                }
            }
            onSheetDismiss()
        }
    }
}