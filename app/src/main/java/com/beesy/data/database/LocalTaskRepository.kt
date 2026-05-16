package com.beesy.data.database

import com.beesy.data.TaskRepository
import com.beesy.data.database.entities.TaskEntity
import com.beesy.data.dto.TaskDto

class LocalTaskRepository(
    private val taskDAO: TaskDAO,
) : TaskRepository {
    override suspend fun insertTask(task: TaskDto) {
        val maxPosition = taskDAO.getMaxPosition()
        taskDAO.insertTask(mapDtoToEntity(task.copy(order = maxPosition + 1)))
    }

    override suspend fun getAllTasks(): List<TaskDto> = taskDAO.getAllTasks().map { mapEntityToDto(it) }

    override suspend fun updateItem(task: TaskDto) {
        taskDAO.updateTask(mapDtoToEntity(task))
    }

    override suspend fun deleteTask(task: TaskDto) {
        taskDAO.deleteTask(mapDtoToEntity(task))
    }

    override suspend fun saveSorting(taskList: List<TaskDto>) {
        taskDAO.saveSorting(taskList.map { mapDtoToEntity(it) })
    }

    override suspend fun deleteMultipleTasks(taskList: List<TaskDto>) {
        taskDAO.deleteMultipleTasks(
            taskList.map { mapDtoToEntity(it) },
        )
    }

    private fun mapDtoToEntity(taskDto: TaskDto): TaskEntity =
        TaskEntity(
            id = taskDto.id,
            title = taskDto.title,
            checked = taskDto.checked,
            position = taskDto.order,
        )

    private fun mapEntityToDto(taskEntity: TaskEntity): TaskDto =
        TaskDto(
            id = taskEntity.id,
            title = taskEntity.title,
            checked = taskEntity.checked,
            order = taskEntity.position,
        )
}