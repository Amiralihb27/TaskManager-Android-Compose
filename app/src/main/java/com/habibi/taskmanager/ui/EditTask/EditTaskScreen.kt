package com.habibi.taskmanager.ui.EditTask


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.text.format.DateFormat
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.platform.LocalContext
import com.habibi.taskmanager.ui.EditTask.EditTaskViewModel
import com.habibi.taskmanager.ui.categories.CategoryDetails
import com.habibi.taskmanager.ui.components.DueDatePickerSection
import com.habibi.taskmanager.ui.components.FilterCategoryRow
import com.habibi.taskmanager.ui.task.TasksDetails
import java.util.Calendar
import java.util.TimeZone
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTaskScreen(
    navigateBack: () -> Unit,
    viewModel: EditTaskViewModel,
    taskId: Int
) {
    LaunchedEffect(key1 = taskId) {
        viewModel.loadTask(taskId)
    }

    val uiState by viewModel.uiState.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Back button
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // 🗑️ Delete icon
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "Delete Task",
                            )
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        when {
            uiState.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            uiState.taskDetails != null -> {
                val taskDetails = uiState.taskDetails!!
                EditTaskForm(
                    taskDetails = taskDetails,
                    allCategories = uiState.categoryList,
                    onCategoryChange = viewModel::onCategoryChange,
                    onTitleChange = viewModel::onTitleChange,
                    onDueDateChange = viewModel::onDueDateChange,
                    onSave = viewModel::saveTask,
                    onDescriptionChange = viewModel::onDescriptionChange,
                    modifier = Modifier.padding(paddingValues)
                )
            }

            else -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Task not found.")
                }
            }
        }

        // Delete confirmation dialog
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Delete Task") },
                text = { Text("Are you sure you want to delete this task? This action cannot be undone.") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.deleteTask(onDeleted = navigateBack)
                            showDeleteDialog = false
                        }
                    ) {
                        Text("Yes", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTaskForm(
    taskDetails: TasksDetails,
    allCategories: List<CategoryDetails>,
    onCategoryChange: (Int?) -> Unit,
    onTitleChange: (String) -> Unit,
    onDueDateChange: (Long?) -> Unit,
    onSave: () -> Unit,
    onDescriptionChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var selectedDateMillis by remember { mutableStateOf<Long?>(null) }

    val formattedDateString = taskDetails.dueDate?.let { millis ->
        DateFormat.format("d MMMM yyyy HH:mm", Date(millis)).toString()
    } ?: "Set due date"

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        // --- Task Title ---
        TextField(
            value = taskDetails.title,
            onValueChange = { newValue ->
                onTitleChange(newValue)
                onSave()
            },
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.SemiBold),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        // --- Category Section ---
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Bookmark,
                contentDescription = "Bookmark",
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            FilterCategoryRow(
                allCategories = allCategories,
                selectedCategoryId = taskDetails.categoryId,
                onCategoryClick = onCategoryChange,
                onSave = onSave
            )
        }

        // --- Task Description ---
        TextField(
            value = taskDetails.description,
            onValueChange = { newValue ->
                onDescriptionChange(newValue)
                onSave()
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Description,
                    contentDescription = "Description",
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 0.dp),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
            )
        )
        // --- Alarm Section ---
        DueDatePickerSection(
            dueDate = taskDetails.dueDate,
            onDueDateChange = {
                onDueDateChange(it)
                onSave()
            },
            modifier = Modifier.padding(start = 0.dp)
        )


    }
}


