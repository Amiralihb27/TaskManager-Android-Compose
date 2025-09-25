package com.habibi.taskmanager.ui.search


import com.habibi.taskmanager.data.entities.Category
import com.habibi.taskmanager.data.entities.Task
import com.habibi.taskmanager.data.repository.CategoriesRepository
import com.habibi.taskmanager.data.repository.TasksRepository
import com.habibi.taskmanager.ui.task.TasksDetails
import com.habibi.taskmanager.ui.task.TasksViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
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


class SearchViewModelTest {

    private val tasksRepository: TasksRepository = mockk()
    private val categoriesRepository: CategoriesRepository = mockk()
    private lateinit var viewModel: SearchViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        val pendingTasks =
            listOf(
                Task(taskId = 1, title = "watch movie", categoryId = 1, description = ""),
                Task(taskId = 3, title = "read book", categoryId = 1, description = "qw")
            )
        val completedTasks =
            listOf(Task(taskId = 2, title = "buy milk", categoryId = 2, description = ""))
        val categories =
            listOf(
                Category(categoryId = 1, name = "Work", color = "#FF5733"),
                Category(2, "Home", "#33FF57")
            )

        coEvery { tasksRepository.getAllPendingTasks() } returns flowOf(pendingTasks)
        coEvery { tasksRepository.getAllCompletedTasks() } returns flowOf(completedTasks)
        coEvery { categoriesRepository.getAllCategories() } returns flowOf(categories)
        viewModel = SearchViewModel(tasksRepository, categoriesRepository)

    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init loads tasks and categories into uiState`() = runTest {

        viewModel = SearchViewModel(tasksRepository, categoriesRepository)
        advanceUntilIdle()

        val uiState = viewModel.uiState.value
        assertEquals(3, uiState.tasksList.size)
        assertEquals("watch movie", uiState.tasksList[0].title)
        assertEquals("read book", uiState.tasksList[1].title)
        assertEquals("buy milk", uiState.tasksList[2].title)
        assertEquals(2, uiState.categories.size)
        assertEquals("Work", uiState.categories[0].name)
    }

    @Test
    fun `onFilterByTitle updates searchedTitle`() = runTest {
        viewModel.onFilterByTitle("watch")
        advanceUntilIdle()


        var uiState = viewModel.uiState.value
        assertEquals("watch", uiState.searchedTitle)
        assertEquals(1, uiState.filteredTaskList.size)
        viewModel.onFilterByTitle("m")
        advanceUntilIdle()


        uiState = viewModel.uiState.value
        assertEquals("m", uiState.searchedTitle)
        assertEquals(2, uiState.filteredTaskList.size)
    }

    @Test
    fun `onFilterByTitle with null does not update searchedTitle`() = runTest {

        viewModel.onFilterByTitle(null)
        advanceUntilIdle()

        val uiState = viewModel.uiState.value
        assertEquals(null, uiState.searchedTitle)
        assertEquals(3, uiState.filteredTaskList.size)
    }
}
