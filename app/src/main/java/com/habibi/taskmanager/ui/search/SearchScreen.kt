package com.habibi.taskmanager.ui.search

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Card
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.com.habibi.ui.search.SearchScreenUiState
import com.habibi.taskmanager.ui.categories.CategoryDetails


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel,
    navigateToTaskUpdate: (Int) -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()
    Scaffold(
    ) { paddingValues ->
        SearchScreen(modifier.padding(paddingValues), viewModel, uiState, navigateToTaskUpdate)
    }

}


@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel,
    taskUiState: SearchScreenUiState,
    navigateToTaskUpdate: (Int) -> Unit
) {
    Box(
        modifier = Modifier,
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            SearchBox(viewModel::onFilterByTitle,taskUiState.searchedTitle)
            ShowTasks(modifier, taskUiState, navigateToTaskUpdate)
        }

    }
}

@Composable
fun SearchBox(
    onFilter: (String?) -> Unit,
    searchedTitle:String?
) {
    var query by remember { mutableStateOf("") }
    TextField(
        value = searchedTitle?:"",
        onValueChange = {
            query = it
            onFilter(it)
        },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Color.Gray
            )
        },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )

}


@Composable
fun ShowTasks(
    modifier: Modifier,
    taskUiState: SearchScreenUiState,
    navigateToTaskUpdate: (Int) -> Unit
) {
    if (taskUiState.filteredTaskList.isEmpty()) {
        EmptyTasks(modifier)
    } else {
        FilteredTasksList(taskUiState, navigateToTaskUpdate)
    }

}

@Composable
private fun FilteredTasksList(
    taskUiState: SearchScreenUiState,
    navigateToTaskUpdate: (Int) -> Unit
) {
    val filteredTasks = taskUiState.filteredTaskList
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
//        contentPadding = PaddingValues(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(filteredTasks, key = { it.taskId }) { task ->
            val category: CategoryDetails? =
                taskUiState.categories.find { it.categoryId == task.categoryId }
            val categoryColorString = category?.color ?: "0xFF757575"
            val categoryColor = Color(
                android.graphics.Color.parseColor(categoryColorString)
            )

            FilterRowItem(
                color = categoryColor,
                title = task.title,
                isDone = task.status.toString()=="DONE",
                onClick = { navigateToTaskUpdate(task.taskId) }
            )
        }

    }
}


//should be customize
@Composable
fun EmptyTasks(modifier: Modifier) {
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
                tint = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "No Tasks",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun TasksRow() {

}

@Composable
fun FilterRowItem(
    color: Color,
    title: String,
    isDone:Boolean,
    bgColor: Color = Color.Transparent,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(bgColor, shape = RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Small colored circle
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(color, shape = CircleShape)
        )
        Spacer(modifier = Modifier.width(12.dp))
        // Title text
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge.copy(
                textDecoration = if (isDone) TextDecoration.LineThrough else TextDecoration.None
            )
//            color = Color.White
        )
    }

}

//@Preview(
//    name = "FilterRowItem Preview",
//    showBackground = true
//)
//@Composable
//fun FilterRowItemPreview() {
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .background(Color.Black)
//            .padding(16.dp),
//        verticalArrangement = Arrangement.spacedBy(8.dp)
//    ) {
//        FilterRowItem(
//            color = Color.Red,
//            title = "High Priority",
//            bgColor = Color.DarkGray,
//            onClick = {}
//        )
//        FilterRowItem(
//            color = Color.Green,
//            title = "Completed",
//            bgColor = Color.DarkGray,
//            onClick = {}
//        )
//        FilterRowItem(
//            color = Color.Blue,
//            title = "In Progress",
//            bgColor = Color.DarkGray,
//            onClick = {}
//        )
//    }
//}


//@Preview(
//    name = "Search Screen - Dark Mode",
//    showBackground = true,
//    uiMode = Configuration.UI_MODE_NIGHT_YES
//)
//@Composable
//fun SearchScreenDarkPreview() {
//    SearchScreen()
//}
