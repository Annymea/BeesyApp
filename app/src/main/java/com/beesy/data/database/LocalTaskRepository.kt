package com.beesy.data.database

import com.beesy.data.TaskRepository
import com.beesy.data.database.entities.SkillEntity
import com.beesy.data.database.entities.SkillToTaskEntity
import com.beesy.data.database.entities.TaskEntity
import com.beesy.data.dto.SkillDto
import com.beesy.data.dto.TaskDto

class LocalTaskRepository(
    private val taskDAO: TaskDAO,
) : TaskRepository {
    override suspend fun insertTask(task: TaskDto) {
        val maxPosition = taskDAO.getMaxPosition()
        taskDAO.insertTask(task.copy(order = maxPosition + 1).toEntity())
    }

    override suspend fun getAllTasks(): List<TaskDto> {
        taskDAO.getAllTasks().map { it.toDto() }
        return TODO("Provide the return value")
    }

    override suspend fun updateItem(task: TaskDto) {
        taskDAO.updateTask(task.toEntity())
    }

    override suspend fun deleteTask(task: TaskDto) {
        taskDAO.deleteTask(task.toEntity())
    }

    override suspend fun saveSorting(taskList: List<TaskDto>) {
        taskDAO.saveSorting(taskList.map { it.toEntity() })
    }

    override suspend fun deleteMultipleTasks(taskList: List<TaskDto>) {
        taskDAO.deleteMultipleTasks(
            taskList.map { it.toEntity() },
        )
    }

    override suspend fun getAllSkills(): List<SkillDto> = taskDAO.getAllSkills().map { it.toDto() }

    override suspend fun addSkill(skill: SkillDto) {
        taskDAO.insertSkill(skill.toEntity())
    }

    override suspend fun updateSkill(skill: SkillDto) {
        taskDAO.updateSkill(skill.toEntity())
    }

    override suspend fun deleteSkill(skill: SkillDto) {
        taskDAO.deleteSkill(skill.toEntity())
    }

    override suspend fun updateTaskSkills(
        taskId: Int,
        skills: List<Int>,
    ) {
        val existingSkillsOnTask = taskDAO.getSkillsOfTask(taskId).map { it.skillId }
        val addingSkillIds = skills - existingSkillsOnTask.toSet()
        val deletingSkillIds = existingSkillsOnTask - skills.toSet()

        if (addingSkillIds.isNotEmpty()) {
            taskDAO.insertSkillsOfTask(
                addingSkillIds.map { skillId ->
                    SkillToTaskEntity(taskId = taskId, skillId = skillId)
                },
            )
        }

        if (deletingSkillIds.isNotEmpty()) {
            deletingSkillIds
                .map { skillId ->
                    SkillToTaskEntity(taskId = taskId, skillId = skillId)
                }.forEach { taskDAO.deleteSkillFromTask(it) }
        }
    }

    private fun TaskDto.toEntity(): TaskEntity =
        TaskEntity(
            id = id,
            title = title,
            checked = checked,
            position = order,
        )

    private fun TaskEntity.toDto(): TaskDto =
        TaskDto(
            id = id,
            title = title,
            checked = checked,
            order = position,
        )

    private fun SkillEntity.toDto(): SkillDto =
        SkillDto(
            id = id,
            title = title,
            experience = experience,
            level = level,
        )

    private fun SkillDto.toEntity(): SkillEntity =
        SkillEntity(
            id = id,
            title = title,
            experience = experience,
            level = level,
        )
}