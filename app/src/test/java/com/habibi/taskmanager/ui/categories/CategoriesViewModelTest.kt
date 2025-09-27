package com.habibi.taskmanager.ui.categories


import com.habibi.taskmanager.data.entities.Category
import com.habibi.taskmanager.data.repository.CategoriesRepository
import com.habibi.taskmanager.data.repository.FakeCategoriesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CategoriesViewModelTest {

    private lateinit var viewModel: CategoriesViewModel
    private lateinit var fakeRepository: FakeCategoriesRepository

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        this.fakeRepository = FakeCategoriesRepository()
        viewModel = CategoriesViewModel(this.fakeRepository)
    }

    @After
    fun TearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onSheetDismiss sets isSheetShown to false`() {
        viewModel.onAddCategoryClicked()
        assertTrue(viewModel.uiState.value.isSheetShown)

        viewModel.onSheetDismiss()
        assertFalse(viewModel.uiState.value.isSheetShown)
    }

    @Test
    fun `onCategoryClicked sets sheets to shown and reset details`() {
        viewModel.onAddCategoryClicked()
        val uiState = viewModel.uiState.value
        assertTrue(uiState.isSheetShown)
        assertEquals("", uiState.categoryDetails.name)
        assertFalse(uiState.categoryDetails.isEntryValid)
//        assertTrue  (uiState.categoryDetails.isEntryValid)
    }

    @Test
    fun `updateCategoryDetails with blank names sets validationToFalse`() {
        viewModel.onAddCategoryClicked()
        viewModel.updateCategoryDetails(CategoryDetails(name = "   "))
        val uiState = viewModel.uiState.value
        assertFalse(
            uiState.categoryDetails.isEntryValid,
        )
    }

    @Test
    fun `updateCategoryDetails with valid name  sets validation To True`() {
        viewModel.onAddCategoryClicked()
        viewModel.updateCategoryDetails(CategoryDetails(name = "Personal"))
        val uiState = viewModel.uiState.value
        assertTrue(
            uiState.categoryDetails.isEntryValid
        )
    }

    @Test
    fun `onSaveCategory with invalid entry does nothing`() = runTest {

        viewModel.onAddCategoryClicked()
        viewModel.updateCategoryDetails(CategoryDetails(name = ""))

        viewModel.onSaveCategory()

        assertTrue(fakeRepository.logged.isEmpty())
    }

    @Test
    fun `onSaveCategory with valid new entry calls insertCategory`() = runTest {
        viewModel.onAddCategoryClicked()
        viewModel.updateCategoryDetails(CategoryDetails(name = "Work", color = "#FF5733"))
        viewModel.onSaveCategory()

        advanceUntilIdle()

        assertEquals(1, fakeRepository.logged.size)
        assertEquals("insertCategory: Work", fakeRepository.logged.first())
    }

    @Test
    fun `onEditCategoryClicked sets sheet to shown and loads correct category details`() {
        val categoryToEdit = CategoryDetails(
            categoryId = 1,
            name = "Personal",
            color = "#FF5733",
            isEntryValid = true
        )

        viewModel.onEditCategoryClicked(categoryToEdit)

        val uiState = viewModel.uiState.value
        assertTrue(uiState.isSheetShown)
        assertEquals(categoryToEdit, uiState.categoryDetails)
    }

    @Test
    fun `onDeleteCategory calls deleteCategory in repository and hides sheet`() = runTest {

        val categoryToDelete = CategoryDetails(
            categoryId = 1,
            name = "Personal",
            color = "#FF5733"
        )

        viewModel.onEditCategoryClicked(categoryToDelete)

        assertTrue(viewModel.uiState.value.isSheetShown)

        viewModel.onDeleteCategory()
        advanceUntilIdle()
        assertEquals(1, fakeRepository.logged.size)
        assertEquals("deleteCategory: Personal", fakeRepository.logged.first())
        assertFalse(viewModel.uiState.value.isSheetShown)
    }

        @Test
        fun `init load category from repository and change state`() = runTest {
            val initialCategories = listOf(
                Category(name = "work", color = "FF5733"),
                Category(categoryId = 1,name = "personal", color = "FF5733"),
            )

            val preLoadedFakeRepository = FakeCategoriesRepository(initialCategories)
            val viewModelWithData = CategoriesViewModel(preLoadedFakeRepository)

            advanceUntilIdle()

            val uiState = viewModelWithData.uiState.value

            assertEquals(2, uiState.categoryList.size)
            assertEquals("work", uiState.categoryList.first().name)
            assertEquals(0, uiState.categoryList[0].categoryId)
            assertEquals(1, uiState.categoryList[1].categoryId)
            //db is auto increment but both ids are 0
            assertEquals("personal", uiState.categoryList[1].name)
        }


}