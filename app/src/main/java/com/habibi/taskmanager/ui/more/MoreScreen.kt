package com.habibi.taskmanager.ui.more

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.habibi.taskmanager.ui.AppViewModelProvider


@Composable
fun MoreScreen(modifier: Modifier = Modifier , viewModel: MoreViewModel = viewModel(factory = AppViewModelProvider.Factory)){
    Text("More" , modifier=modifier)
}
