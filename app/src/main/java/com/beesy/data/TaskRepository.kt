package com.beesy.data

import com.beesy.data.dto.TaskDto

// Zugriffspunkt der restlichen App auf die Daten, damit das für den rest der App erstmal irrelevant ist, woher die daten kommen
interface TaskRepository {
    suspend fun insertTask(task: TaskDto)

    suspend fun getAllTasks(): List<TaskDto>

    suspend fun updateItem(task: TaskDto)

    suspend fun deleteTask(task: TaskDto)

    suspend fun saveSorting(taskList: List<TaskDto>)

    suspend fun deleteMultipleTasks(taskList: List<TaskDto>)
}