package com.beesy.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "skill_to_task",
    foreignKeys = [
        ForeignKey(
            entity = TaskEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("task_id"),
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = SkillEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("skill_id"),
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
    ],
    primaryKeys = ["task_id", "skill_id"],
)
data class SkillToTaskEntity(
    @ColumnInfo(name = "task_id")
    val taskId: Int,
    @ColumnInfo(name = "skill_id")
    val skillId: Int,
)