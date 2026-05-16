package com.beesy.data.database

import com.beesy.data.TaskRepository
import com.beesy.data.database.entities.TaskEntity
import com.beesy.data.dto.TaskDto

class LocalTaskRepository(
    private val taskDAO: TaskDAO,
) : TaskRepository {
    override suspend fun insertTask(task: TaskDto) {
        taskDAO.insertTask(mapDtoToEntity(task))
    }

    override suspend fun getAllTasks(): List<TaskDto> = taskDAO.getAllTasks().map { mapEntityToDto(it) }

    override suspend fun updateItem(task: TaskDto) {
        taskDAO.updateTask(mapDtoToEntity(task))
    }

    override suspend fun deleteTask(task: TaskDto) {
        var allTasks = taskDAO.getAllTasks() - mapDtoToEntity(task)
        allTasks = allTasks.mapIndexed { index, entity -> entity.copy(position = index) }

        taskDAO.deleteTask(mapDtoToEntity(task))
        taskDAO.saveSorting(allTasks)
    }

    override suspend fun saveSorting(taskList: List<TaskDto>) {
        taskDAO.saveSorting(taskList.map { mapDtoToEntity(it) })
    }

    override suspend fun deleteMultipleTasks(taskList: List<TaskDto>) {
        var allTasks = taskDAO.getAllTasks() - taskList.map { mapDtoToEntity(it) }.toSet()
        allTasks = allTasks.mapIndexed { index, entity -> entity.copy(position = index) }

        taskDAO.deleteMultipleTasks(
            taskList.map { mapDtoToEntity(it) },
        )

        taskDAO.saveSorting(allTasks)
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