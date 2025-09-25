package com.habibi.taskmanager.ui.EditTask

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

class EditTaskViewModelTest {

    private lateinit var mockingTasksRepository: TasksRepository
    private lateinit var mockingCategoriesRepository: CategoriesRepository
    private lateinit var viewModel: EditTaskViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockingTasksRepository = mockk<TasksRepository>()
        mockingCategoriesRepository = mockk<CategoriesRepository>()

        val tasks = listOf(
            Task(taskId = 1, title = "Work Task", categoryId = 1, description = ""),
            Task(taskId = 2, title = "Personal Task", categoryId = 2, description = "")
        )
        val categories = listOf(
            Category(categoryId = 1, name = "Work", color = "#FF5733"),
            Category(categoryId = 2, name = "Personal", color = "#33FF57")
        )
        coEvery { mockingTasksRepository.getAllPendingTasks() } returns flowOf(tasks)
        coEvery { mockingCategoriesRepository.getAllCategories() } returns flowOf(categories)
        viewModel = EditTaskViewModel(mockingTasksRepository, mockingCategoriesRepository)

    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onTitleChange with blank text uistate will be updated and sets isEntryValid to false`() =
        runTest {
            val task = Task(taskId = 1, title = "Work Task", categoryId = 1, description = "")
            coEvery { mockingTasksRepository.getTask(1) } returns flowOf(task)
            viewModel.loadTask(1)
            advanceUntilIdle()

            viewModel.onTitleChange("   ") // blank title
            advanceUntilIdle()

            val uiState = viewModel.uiState.value
            val taskDetails = uiState.taskDetails
            assertFalse(taskDetails?.isEntryValid ?: true)
        }

    @Test
    fun `onTitleChange with valid input uistate will be updated and sets isEntryValid to True`() =
        runTest {
            val task = Task(taskId = 1, title = "Work Task", categoryId = 1, description = "")
            coEvery { mockingTasksRepository.getTask(1) } returns flowOf(task)
            viewModel.loadTask(1)
            advanceUntilIdle()

            viewModel.onTitleChange("Work") // blank title
            advanceUntilIdle()

            val uiState = viewModel.uiState.value
            val taskDetails = uiState.taskDetails
            assertTrue(taskDetails?.isEntryValid ?: true)
        }

    @Test
    fun `onCategoryChange with no category  sets isEntryValid to False`() =
        runTest {
            val task = Task(taskId = 1, title = "Work Task", categoryId = 1, description = "")
            coEvery { mockingTasksRepository.getTask(1) } returns flowOf(task)
            viewModel.loadTask(1)
            advanceUntilIdle()

            viewModel.onCategoryChange(null) // no category
            advanceUntilIdle()

            val uiState = viewModel.uiState.value
            val taskDetails = uiState.taskDetails

            assertFalse(taskDetails?.isEntryValid ?: true)
        }

    @Test
    fun `onCategoryChange with a category  sets isEntryValid to true`() =
        runTest {
            val task = Task(taskId = 1, title = "Work Task", categoryId = 1, description = "")
            coEvery { mockingTasksRepository.getTask(1) } returns flowOf(task)
            viewModel.loadTask(1)
            advanceUntilIdle()

            viewModel.onCategoryChange(5)
            advanceUntilIdle()

            val uiState = viewModel.uiState.value
            val taskDetails = uiState.taskDetails

            assertTrue(taskDetails?.isEntryValid ?: true)
        }

    @Test
    fun `onDueDateChange updates task dueDate`() = runTest {
        val task = Task(taskId = 1, title = "Work Task", categoryId = 1, description = "")
        coEvery { mockingTasksRepository.getTask(1) } returns flowOf(task)
        viewModel.loadTask(1)
        advanceUntilIdle()

        val newDueDate = 1700000000000L
        viewModel.onDueDateChange(newDueDate)
        advanceUntilIdle()

        val uiState = viewModel.uiState.value
        assertEquals(newDueDate, uiState.taskDetails?.dueDate)
    }


    @Test
    fun `onSave with valid inputs sets isSaved true`() = runTest {
        val slot = slot<Task>()
        val updatedTask =
            Task(taskId = 1, title = "new work task", categoryId = 5, description = "")
        coEvery { mockingTasksRepository.getTask(1) } returns flowOf(updatedTask)
        coEvery { mockingTasksRepository.updateTask(capture(slot)) } returns Unit
        viewModel.loadTask(1)
        advanceUntilIdle()
        viewModel.onCategoryChange(2)
        advanceUntilIdle()
        viewModel.saveTask()
        advanceUntilIdle()
        val uiState = viewModel.uiState.value
        assertTrue(uiState.isTaskSaved)
        assertEquals("new work task", slot.captured.title)
        coVerify(exactly = 1) { mockingTasksRepository.updateTask(any()) }

    }

    @Test
    fun `onSave with invalid inputs does not call updateTask and isTaskSaved stays false`() =
        runTest {
            val task = Task(taskId = 1, title = "", categoryId = 0, description = "")
            coEvery { mockingTasksRepository.getTask(1) } returns flowOf(task)
            viewModel.loadTask(1)
            advanceUntilIdle()

            viewModel.saveTask()
            advanceUntilIdle()

            val uiState = viewModel.uiState.value
            assertFalse(uiState.isTaskSaved)
            coVerify(exactly = 0) { mockingTasksRepository.updateTask(any()) }
        }


}