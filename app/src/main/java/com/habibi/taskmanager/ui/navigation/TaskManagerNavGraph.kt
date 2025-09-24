package com.habibi.taskmanager.ui.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.habibi.taskmanager.ui.AppViewModelProvider
import com.habibi.taskmanager.ui.EditTask.EditTaskScreen
import com.habibi.taskmanager.ui.EditTask.EditTaskViewModel
import com.habibi.taskmanager.ui.categories.CategoriesScreen
import com.habibi.taskmanager.ui.categories.CategoriesViewModel
import com.habibi.taskmanager.ui.more.MoreScreen
import com.habibi.taskmanager.ui.search.SearchScreen
import com.habibi.taskmanager.ui.task.TasksScreen
import com.habibi.taskmanager.ui.task.TasksViewModel

@Composable
fun TaskManagerNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onFabClick: (() -> Unit) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    NavHost(
        navController = navController,
        startDestination = NavigationDestination.Tasks.route,
        modifier = modifier
    ) {
        // --- Bottom Navigation Bar Routes   ---

        composable(route = NavigationDestination.Tasks.route) {
            val viewModel: TasksViewModel = viewModel(factory = AppViewModelProvider.Factory)
            LaunchedEffect(Unit) {
                onFabClick {
                    viewModel.onAddTaskDetails()
                }
            }
            TasksScreen(
                viewModel = viewModel,
                navigateToTaskUpdate = { taskId ->
                    navController.navigate(NavigationDestination.TaskDetails.withArgs(taskId))
                },
                snackbarHostState = snackbarHostState
            )


        }
        composable(
            route = NavigationDestination.TaskDetails.route,
            arguments = listOf(navArgument(NavigationDestination.TASK_ID_ARG) {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments!!.getInt(NavigationDestination.TASK_ID_ARG)
            val viewModel: EditTaskViewModel = viewModel(factory = AppViewModelProvider.Factory)
            EditTaskScreen(
                navigateBack = { navController.popBackStack() },
                viewModel = viewModel,
                taskId = taskId
            )
        }

        composable(route = NavigationDestination.Search.route) {
            SearchScreen(
                viewModel = viewModel(factory = AppViewModelProvider.Factory),
                navigateToTaskUpdate = { taskId ->
                    navController.navigate(NavigationDestination.TaskDetails.withArgs(taskId))
                }
            )
        }

        composable(route = NavigationDestination.Categories.route) {
            val viewModel: CategoriesViewModel = viewModel(factory = AppViewModelProvider.Factory)
            LaunchedEffect(Unit) {
                onFabClick {
                    viewModel.onAddCategoryClicked()
                }
            }
            CategoriesScreen(viewModel = viewModel)
        }

        composable(route = NavigationDestination.More.route) {
            MoreScreen(
                viewModel = viewModel(factory = AppViewModelProvider.Factory)
            )
        }

        //
    }
}