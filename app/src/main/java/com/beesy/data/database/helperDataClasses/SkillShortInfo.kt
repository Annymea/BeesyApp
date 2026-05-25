package com.beesy.data.database.helperDataClasses

import androidx.room.ColumnInfo

data class SkillShortInfo(
    @ColumnInfo(name = "skill_id") val skillId : Int,
    val title: String
)