package com.beesy.data

import com.beesy.data.dto.SkillDto
import com.beesy.data.dto.TaskDto

// Zugriffspunkt der restlichen App auf die Daten, damit das für den rest der App erstmal irrelevant ist, woher die daten kommen
interface TaskRepository {
    suspend fun insertTask(task: TaskDto)

    suspend fun getAllTasks(): List<TaskDto>

    suspend fun updateItem(task: TaskDto)

    suspend fun deleteTask(task: TaskDto)

    suspend fun saveSorting(taskList: List<TaskDto>)

    suspend fun deleteMultipleTasks(taskList: List<TaskDto>)

    suspend fun getAllSkills(): List<SkillDto>

    suspend fun addSkill(skill: SkillDto)

    suspend fun updateSkill(skill: SkillDto)

    suspend fun deleteSkill(skill: SkillDto)

    suspend fun updateTaskSkills(
        taskId: Int,
        skills: List<Int>,
    )
}