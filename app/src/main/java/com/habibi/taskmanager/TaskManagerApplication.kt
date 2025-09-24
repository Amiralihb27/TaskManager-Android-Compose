package com.habibi.taskmanager

import android.app.Application
import com.habibi.taskmanager.data.AppContainer
import com.habibi.taskmanager.data.AppDataContainer

class TaskManagerApplication:Application() {

    lateinit var container : AppContainer
    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}