package com.beesy.data.database

import com.beesy.data.TaskRepository
import com.beesy.data.database.entities.TaskEntity
import com.beesy.data.dto.TaskDto

class LocalTaskRepository(
    private val taskDAO: TaskDAO,
) : TaskRepository {
    override suspend fun insertTask(task: TaskDto) {
        taskDAO.insertTask(
            TaskEntity(
                id = task.id,
                title = task.title,
                checked = task.checked,
            ),
        )
    }

    override suspend fun getAllTasks(): List<TaskDto> =
        taskDAO.getAllTasks().map {
            TaskDto(
                id = it.id,
                title = it.title,
                checked = it.checked,
            )
        }

    override suspend fun updateItem(task: TaskDto) {
        taskDAO.updateTask(
            TaskEntity(
                id = task.id,
                title = task.title,
                checked = task.checked,
            ),
        )
    }

    // Do I really need the whole task here? Should not the id be enough
    override suspend fun deleteTask(task: TaskDto) {
        taskDAO.deleteTask(
            TaskEntity(
                id = task.id,
                title = task.title,
                checked = task.checked,
            ),
        )
    }
}