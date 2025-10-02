package com.habibi.taskmanager

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController


import com.habibi.taskmanager.ui.navigation.NavigationDestination
import com.habibi.taskmanager.ui.navigation.TaskManagerNavGraph

private data class BottomNavItem(
    val destination: NavigationDestination,
    val icon: ImageVector,
    val label: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskManagerApp() {
    val navController = rememberNavController()

    val bottomNavItems = listOf(
        BottomNavItem(NavigationDestination.Tasks, Icons.Default.CheckCircle, "Tasks"),
        BottomNavItem(NavigationDestination.Search, Icons.Default.Search, "Search"),
        BottomNavItem(NavigationDestination.Categories, Icons.Default.Bookmark, "Categories"),
        BottomNavItem(NavigationDestination.More, Icons.Default.MoreHoriz, "More")
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentAppDestination =
        bottomNavItems.find { it.destination.route == currentDestination?.route }?.destination


    var fabOnClick by remember { mutableStateOf<() -> Unit>({}) }
    val showBar: Int? = currentAppDestination?.titleRes
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        topBar = {
            if (currentAppDestination?.titleRes != 0) {
                if (showBar != null) {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                stringResource(
                                    id = currentAppDestination?.titleRes ?: R.string.app_name
                                )
                            )
                        },
//                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
//                            // This sets the background color of the bar
//                            containerColor = MaterialTheme.colorScheme.surface,
//
//                            // This sets the color of the title text
//                            titleContentColor = MaterialTheme.colorScheme.onSurface
//                        )
                    )
                }
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = {
            NavigationBar (
                containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ){
                bottomNavItems.forEach { item ->
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any { it.route == item.destination.route } == true,
                        onClick = {
                            navController.navigate(item.destination.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) },


                    )
                }
            }
        },
        floatingActionButton = {
            val fabVisibleRoutes =
                listOf(NavigationDestination.Tasks.route, NavigationDestination.Categories.route)
            if (currentDestination?.route in fabVisibleRoutes) {
                FloatingActionButton(onClick = fabOnClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_add),
                        contentDescription = "Add Item"
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        TaskManagerNavGraph(
            navController = navController,
            modifier = Modifier.padding(innerPadding),
            onFabClick = { action ->
                fabOnClick = action
            },
            snackbarHostState = snackbarHostState,
        )
    }
}