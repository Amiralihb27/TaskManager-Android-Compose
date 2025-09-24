package com.habibi.taskmanager.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.habibi.taskmanager.ui.categories.CategoryDetails


object CategoryChipColors {
    val chipUnselectedBg = Color.Transparent
    val chipUnselectedBorder = Color(0xFF757575) // Grey
}

@Composable
fun CategoryChip(
    text: String,
    backgroundColor: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()
    val textColor = if (isDark) {
        if (isSelected) Color.Black else Color.White
    } else {
        Color.Black
    }

    val border =
        if (isSelected) null else BorderStroke(1.dp, CategoryChipColors.chipUnselectedBorder)

    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = backgroundColor,
        border = border
    ) {
        Text(
            text = text,
            color = textColor,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Composable
fun FilterCategoryRow(
    allCategories: List<CategoryDetails>,
    selectedCategoryId: Int?,
    onCategoryClick: (Int?) -> Unit,
    showAllChip: Boolean = false,
    onSave: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (showAllChip) {
            item {
                val isAllSelected = selectedCategoryId == null
                CategoryChip(
                    text = "All",
                    backgroundColor = if (isAllSelected) MaterialTheme.colorScheme.primary else CategoryChipColors.chipUnselectedBg,
                    isSelected = isAllSelected,
                    onClick = {
                        onCategoryClick(null)

                    }
                )
            }
        }

        items(allCategories) { category ->
            val isSelected = category.categoryId == selectedCategoryId
            val bgColor = if (isSelected) Color(android.graphics.Color.parseColor(category.color))
            else CategoryChipColors.chipUnselectedBg
            CategoryChip(
                text = category.name,
                backgroundColor = bgColor,
                isSelected = isSelected,
                onClick = {
                    onCategoryClick(category.categoryId)
                    onSave?.invoke()
                }
            )
        }
    }
}

