package com.habibi.taskmanager.ui.task


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SnackbarDuration

import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.habibi.taskmanager.ui.AppViewModelProvider
import com.habibi.taskmanager.ui.categories.CategoryDetails
import com.habibi.taskmanager.ui.components.CategoryChip
import com.habibi.taskmanager.ui.components.FilterCategoryRow
import kotlinx.coroutines.launch


@Composable
fun TasksScreen(
    modifier: Modifier = Modifier,
    viewModel: TasksViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navigateToTaskUpdate: (Int) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    val uiState by viewModel.uiState.collectAsState()
    TasksScreenContent(modifier, uiState, viewModel, navigateToTaskUpdate, snackbarHostState)

}


@Composable
fun TasksScreenContent(
    modifier: Modifier = Modifier,
    taskUiState: TasksScreenUiState,
    taskViewModel: TasksViewModel,
    navigateToTaskUpdate: (Int) -> Unit,
    snackbarHostState: SnackbarHostState
) {

    if (taskUiState.isSheetShown) {
        TasksEditBottomSheet(
            tasksDetails = taskUiState.tasksDetails,
            allCategories = taskUiState.categories,
            onDetailsChange = taskViewModel::updateTaskDetails,
            onDismiss = taskViewModel::onSheetDismiss,
            onSave = taskViewModel::onSaveTask
        )
    }

    Column(
        Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        FilterCategoryRow(
            allCategories = taskUiState.categories,
            selectedCategoryId = taskUiState.selectedFilterCategoryId, // Pass the filter state
            onCategoryClick = taskViewModel::onFilterByCategory, // Pass the filter function
            showAllChip = true
        )
        if (taskUiState.filteredTaskList.isEmpty()) {
            EmptyTasks(modifier = modifier.fillMaxSize())
        } else {
            ShowTasks(modifier, taskUiState, taskViewModel, navigateToTaskUpdate, snackbarHostState)
        }

    }

}


@Composable
fun EmptyTasks(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.ThumbUp,
                contentDescription = "No Tasks",
                modifier = Modifier.size(128.dp),
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
            Text(
                text = "No Tasks",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }

}

@Composable
fun ShowTasks(
    modifier: Modifier,
    taskUiState: TasksScreenUiState,
    taskViewModel: TasksViewModel,
    navigateToTaskUpdate: (Int) -> Unit,
    snackbarHostState: SnackbarHostState
) {

    val coroutineScope = rememberCoroutineScope()
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
//        contentPadding = PaddingValues(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val filteredTasks = taskUiState.filteredTaskList
        items(filteredTasks, key = { it.taskId }) { task ->
            val category: CategoryDetails? =
                taskUiState.categories.find { it.categoryId == task.categoryId }
            val categoryColorString = category?.color ?: "0xFF757575"
            val categoryColor = Color(
                android.graphics.Color.parseColor(categoryColorString)
            )
            //snack bar
            TaskCard(
                task.title,
                categoryColor,
                isSelected = false,
                onCardClick = { navigateToTaskUpdate(task.taskId) },
                onRadioBtnClick = {
                    taskViewModel.onDoneTask(task)
                    coroutineScope.launch {
                        val result = snackbarHostState.showSnackbar(
                            message = "task deleted",
                            actionLabel = "Undo",
                            duration = SnackbarDuration.Short
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            taskViewModel.onPendingTask(task)
                        }
                    }

                },
                Modifier
            )
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksEditBottomSheet(
    tasksDetails: TasksDetails,
    allCategories: List<CategoryDetails>,
    onDetailsChange: (TasksDetails) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 15.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            OutlinedTextField(
                value = tasksDetails.title,
                onValueChange = { onDetailsChange(tasksDetails.copy(title = it)) },
                label = { Text("Task") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = !tasksDetails.isEntryValid && tasksDetails.title.isNotEmpty(),
            )
            CategoriesRow(allCategories, tasksDetails, onDetailsChange)
            Button(
                onClick = onSave,
                enabled = tasksDetails.isEntryValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(0.dp)

            ) {
                Text("Save", fontSize = 18.sp)
            }

        }
    }

}

@Composable
private fun CategoriesRow(
    allCategories: List<CategoryDetails>,
    tasksDetails: TasksDetails,
    onDetailsChange: (TasksDetails) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(allCategories) { category ->
            val isThisChipSelected = (category.categoryId == tasksDetails.categoryId)
            val chipBackgroundColor = if (isThisChipSelected) {
                Color(android.graphics.Color.parseColor(category.color))
            } else {
                CategoryChipColors.chipUnselectedBg
            }

            CategoryChip(
                text = category.name,
                backgroundColor = chipBackgroundColor,
                isSelected = isThisChipSelected,
                onClick = { onDetailsChange(tasksDetails.copy(categoryId = category.categoryId)) }
            )
        }
    }
}

object CategoryChipColors {
    val chipUnselectedBg = Color.Transparent
    val chipUnselectedBorder = Color(0xFF757575) // Grey
}


@Composable
fun TaskCard(
    taskTitle: String,
    color: Color,
    isSelected: Boolean,
    onCardClick: () -> Unit,
    onRadioBtnClick: () -> Unit,
    modifier: Modifier
) {

    Card(
        onClick = onCardClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)

    ) {
        Row(
            modifier = Modifier
                .height(80.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(12.dp)
                    .fillMaxHeight()
                    .background(color)
            )

            RadioButton(
                selected = isSelected,
                onClick = onRadioBtnClick,
                modifier = Modifier.padding(start = 12.dp, end = 6.dp)
            )
            Text(
                text = taskTitle,
                fontSize = 16.sp,
                modifier = Modifier
                    .weight(1f)

            )
        }
    }
}


