package com.habibi.taskmanager

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.view.WindowCompat
import com.habibi.taskmanager.ui.theme.TaskManagerAppTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            TaskManagerAppTheme {
                TaskManagerApp()
                NotificationPermissionRequester()
            }
        }
    }
}

@Composable
fun NotificationPermissionRequester() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted: Boolean ->
                if (isGranted) {
                    // log user action
                } else {
                    //  log user action
                }
            }
        )

        LaunchedEffect(key1 = Unit) {
            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}
