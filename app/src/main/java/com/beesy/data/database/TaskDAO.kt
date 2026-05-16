package com.beesy.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.beesy.data.database.entities.TaskEntity

@Dao
interface TaskDAO {
    @Query("SELECT * FROM task ORDER BY position ASC")
    fun getAllTasks(): List<TaskEntity>

    @Insert
    suspend fun insertTask(task: TaskEntity)

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Delete
    suspend fun deleteTask(task: TaskEntity)

    @Delete fun deleteMultipleTasks(taskList: List<TaskEntity>)

    @Update
    suspend fun saveSorting(task: List<TaskEntity>)
}