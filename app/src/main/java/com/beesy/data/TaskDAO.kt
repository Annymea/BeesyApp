package com.beesy.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

// Hier gibt es die Anfragen an die Datenbank
@Dao
interface TaskDAO {
    @Query("SELECT * FROM task")
    fun getAllTasks(): List<Task>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTask(task: Task)
}