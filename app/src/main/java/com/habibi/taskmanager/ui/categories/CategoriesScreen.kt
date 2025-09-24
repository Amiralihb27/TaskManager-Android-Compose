package com.habibi.taskmanager.ui.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.habibi.taskmanager.ui.AppViewModelProvider


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(
    modifier: Modifier = Modifier,
    viewModel: CategoriesViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    CategoriesScreenContent(uiState, viewModel, modifier)
}

@Composable
private fun CategoriesScreenContent(
    uiState: CategoriesScreenUiState,
    viewModel: CategoriesViewModel,
    modifier: Modifier
) {
    if (uiState.isSheetShown) {
        CategoryEditBottomSheet(
            categoryDetails = uiState.categoryDetails,
            colorOptions = uiState.colorOptions,
            onDetailsChange = viewModel::updateCategoryDetails,
            onDismiss = viewModel::onSheetDismiss,
            onSave = viewModel::onSaveCategory,
            onDelete = viewModel::onDeleteCategory
        )
    }

    if (uiState.categoryList.isEmpty()) {
        EmptyCategoriesView(modifier = modifier.fillMaxSize())
    } else {
        VisibleCategoriesView(modifier, uiState, viewModel)
    }
}

@Composable
private fun VisibleCategoriesView(
    modifier: Modifier,
    uiState: CategoriesScreenUiState,
    viewModel: CategoriesViewModel
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(uiState.categoryList, key = { it.categoryId }) { category ->
            CategoryCard(
                category = category,
                onClick = { viewModel.onEditCategoryClicked(category) }
            )
        }
    }
}

@Composable
private fun EmptyCategoriesView(modifier: Modifier = Modifier) {
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
                contentDescription = "No categories",
                modifier = Modifier.size(128.dp),
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
            Text(
                text = "No categories",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
private fun CategoryCard(
    category: CategoryDetails,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        Color(android.graphics.Color.parseColor(category.color))
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Bookmark,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(text = category.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryEditBottomSheet(
    categoryDetails: CategoryDetails,
    colorOptions: List<String>,
    onDetailsChange: (CategoryDetails) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit,
    onDelete: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp), // Extra padding for when keyboard is open
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            OutlinedTextField(
                value = categoryDetails.name,
                onValueChange = { onDetailsChange(categoryDetails.copy(name = it)) },
                label = { Text("Category") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = !categoryDetails.isEntryValid && categoryDetails.name.isNotEmpty(),
                trailingIcon = {
                    if (categoryDetails.categoryId != 0) {
                        IconButton(onClick = onDelete) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete Category")
                        }
                    }
                }
            )

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(colorOptions) { colorString ->
                    val color = Color(android.graphics.Color.parseColor(colorString))
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(color)
                            .clickable { onDetailsChange(categoryDetails.copy(color = colorString)) }
                            .then(
                                if (colorString == categoryDetails.color) Modifier.border(
                                    2.dp,
                                    MaterialTheme.colorScheme.onSurface,
                                    CircleShape
                                ) else Modifier
                            )
                    )
                }
            }

            Button(
                onClick = onSave,
                enabled = categoryDetails.isEntryValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(
                        android.graphics.Color.parseColor(categoryDetails.color)
                    )
                )
            ) {
                Text("Save", fontSize = 16.sp)
            }
        }
    }
}