package com.habibi.taskmanager.ui.task

import com.habibi.taskmanager.data.entities.Category
import com.habibi.taskmanager.data.entities.Task
import com.habibi.taskmanager.data.entities.TaskStatus
import com.habibi.taskmanager.data.repository.CategoriesRepository
import com.habibi.taskmanager.data.repository.TasksRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
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

class TasksViewModelWithMockkTest {

    private lateinit var mockTaskRepository: TasksRepository
    private lateinit var mockCategoryRepository: CategoriesRepository
    private lateinit var viewModel: TasksViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockTaskRepository = mockk<TasksRepository>()
        mockCategoryRepository = mockk<CategoriesRepository>()

        coEvery {
            mockTaskRepository.getAllPendingTasks()
        } returns emptyFlow()

        coEvery {
            mockCategoryRepository.getAllCategories()
        } returns emptyFlow()

        viewModel = TasksViewModel(mockTaskRepository, mockCategoryRepository)

    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `validInput with blank input returns false`() {
        viewModel.updateTaskDetails(TasksDetails(title = "  ", categoryId = 1))
        val uiState = viewModel.uiState.value
        assertFalse(uiState.tasksDetails.isEntryValid)
    }

    @Test
    fun `validateInput with no category returns false`() {

        viewModel.updateTaskDetails(
            TasksDetails(
                title = "Valid Title",
                categoryId = 0
            )
        ) // categoryId=0 means none selected))
        val uiState = viewModel.uiState.value
        assertFalse(uiState.tasksDetails.isEntryValid)
    }

    @Test
    fun `onDoneTask calls updateTask in repository with DONE status`() = runTest {

        val TaskToDone = TasksDetails(
            taskId = 1,
            title = "Finish the test",
            status = TaskStatus.PENDING,
            categoryId = 1
        )
        val slot = slot<Task>()
        coEvery { mockTaskRepository.updateTask(capture(slot)) } returns Unit
        viewModel.onDoneTask(TaskToDone)
        advanceUntilIdle()

        coVerify { mockTaskRepository.updateTask(task = any()) }
        assertEquals("Finish the test", slot.captured.title)

    }

    @Test
    fun `onAllCategories display all tasks`() = runTest {
        val tasks = listOf(
            Task(taskId = 1, title = "Work Task", categoryId = 1, description = ""),
            Task(taskId = 2, title = "Personal Task", categoryId = 2, description = "")
        )
        coEvery { mockTaskRepository.getAllPendingTasks() }.returns(flowOf(tasks))
        coEvery { mockCategoryRepository.getAllCategories() } returns emptyFlow()
        viewModel = TasksViewModel(mockTaskRepository, mockCategoryRepository)
        advanceUntilIdle()

        val uiState = viewModel.uiState.value
        assertEquals(2, uiState.filteredTaskList.size)
        assertEquals("Work Task", uiState.filteredTaskList[0].title)
    }

    @Test
    fun `onFilterByCategory updates state and filters list`() = runTest {
        val tasks = listOf(
            Task(taskId = 1, title = "Work Task", categoryId = 1, description = ""),
            Task(taskId = 2, title = "Personal Task", categoryId = 2, description = "")
        )
        val categories = listOf(
            Category(categoryId = 1, name = "Work", color = "#FF5733"),
            Category(categoryId = 2, name = "Personal", color = "#33FF57")
        )
        coEvery { mockTaskRepository.getAllPendingTasks() }.returns(flowOf(tasks))
        coEvery { mockCategoryRepository.getAllCategories() } returns (flowOf(categories))
        viewModel = TasksViewModel(mockTaskRepository, mockCategoryRepository)
        viewModel.onFilterByCategory(categoryId = 2)
        advanceUntilIdle()

        val uiState = viewModel.uiState.value
        assertEquals(1, uiState.filteredTaskList.size)
        assertEquals("Personal Task", uiState.filteredTaskList[0].title)
    }

    @Test
    fun `onAddTaskDetails sets isSheetShown to true`() {
        viewModel.onAddTaskDetails()
        val uiState = viewModel.uiState.value
        assertEquals(true, uiState.isSheetShown)
    }

    @Test
    fun `onSaveTaskDetails sets isSheetShown to false and calls insert method`() = runTest {
        val newTaskDetails =
            TasksDetails(taskId = 0, title = "Test Task", categoryId = 1, description = "")
        //new task has 0 as id
        val slot = slot<Task>()
        coEvery { mockTaskRepository.insertTask(capture(slot)) } returns Unit
        coEvery { mockTaskRepository.updateTask(any()) } returns Unit

        val viewModel = TasksViewModel(mockTaskRepository, mockCategoryRepository)

        viewModel.updateTaskDetails(newTaskDetails)
        viewModel.onSaveTask()

        advanceUntilIdle()

        val uiState = viewModel.uiState.value

        coVerify { mockTaskRepository.insertTask(any()) }
        assertEquals("Test Task", slot.captured.title)
        assertFalse { uiState.isSheetShown }

    }


}