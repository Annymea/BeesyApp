package com.beesy.data.dto

data class SkillDto(
    val id: Int = 0,
    val title: String,
    val experience: Int = 0,
    val level: Int = 1,
)

data class MinSkillDto(
    val id: Int,
    val title: String,
)