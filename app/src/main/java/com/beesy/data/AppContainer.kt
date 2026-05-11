package com.beesy.data

import android.content.Context

// Wichtig für die Dependency Injection
interface AppContainer {
    val taskRepository: TaskRepository
}

class AppDataContainer(
    private val context: Context,
) : AppContainer {
    override val taskRepository: TaskRepository by lazy {
        LocalTaskRepository(TaskDB.getDatabase(context).taskDAO())
    }
}