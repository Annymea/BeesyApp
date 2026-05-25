package com.beesy.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "skill")
data class SkillEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo val title: String,
    @ColumnInfo val experience: Int,
    @ColumnInfo val level: Int,
)