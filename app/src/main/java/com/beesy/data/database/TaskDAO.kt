package com.beesy.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.beesy.data.database.entities.SkillEntity
import com.beesy.data.database.entities.SkillToTaskEntity
import com.beesy.data.database.entities.TaskEntity

@Dao
interface TaskDAO {
    @Query("SELECT id, title, checked FROM task ORDER BY position ASC")
    fun getAllTasks(): List<TaskEntity>

    @Query("SELECT MAX(position) FROM task")
    fun getMaxPosition(): Int

    @Query("SELECT id, title, experience, level FROM skill")
    fun getAllSkills(): List<SkillEntity>

    @Query(
        "SELECT skill_id, title " +
            "FROM skill_to_task " +
            "JOIN skill ON skill_id = id " +
            "WHERE task_id = :taskId",
    )
    fun getSkillsOfTask(taskId: Int): List<Pair<Int, String>>

    @Insert
    suspend fun insertTask(task: TaskEntity)

    @Insert
    suspend fun insertSkillsOfTask(skillList: List<SkillToTaskEntity>)

    @Insert
    suspend fun insertSkill(skill: SkillEntity)

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Update
    suspend fun saveSorting(task: List<TaskEntity>)

    @Update
    suspend fun updateSkill(skill: SkillEntity)

    @Delete
    suspend fun deleteTask(task: TaskEntity)

    @Delete fun deleteMultipleTasks(taskList: List<TaskEntity>)

    @Delete fun deleteSkillFromTask(taskSkill: SkillToTaskEntity)

    @Delete fun deleteSkill(skill: SkillEntity)
}