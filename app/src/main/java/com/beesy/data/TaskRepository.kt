package com.beesy.data

// Zugriffspunkt der restlichen App auf die Daten, damit das für den rest der App erstmal irrelevant ist, woher die daten kommen
interface TaskRepository {
    suspend fun insertTask(task: Task)

    suspend fun getAllTasks(): List<Task>

    suspend fun updateItem(task: Task)
}