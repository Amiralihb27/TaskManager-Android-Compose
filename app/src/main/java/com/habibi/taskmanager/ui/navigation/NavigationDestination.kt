package com.habibi.taskmanager.ui.navigation

import androidx.annotation.StringRes
import com.habibi.taskmanager.R


sealed class NavigationDestination(val route: String, @StringRes val titleRes: Int) {

    object Tasks : NavigationDestination("tasks_route", R.string.bottom_nav_tasks)
    object Search : NavigationDestination("search_route", R.string.bottom_nav_search)
    object Categories : NavigationDestination("categories_route", R.string.bottom_nav_categories)
    object More : NavigationDestination("more_route", R.string.bottom_nav_more)

    companion object {
        const val TASK_ID_ARG = "taskId"
    }
    object TaskDetails : NavigationDestination("task_detail_route/{$TASK_ID_ARG}",0){
        fun withArgs(taskId: Int): String {
            return route.replace("{$TASK_ID_ARG}", taskId.toString())
        }
    }


}