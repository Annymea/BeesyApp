package com.beesy.data.dto

data class SkillDto(
    val id: Int = 0,
    val title: String,
    val experience: Int,
    val level: Int,
)

data class MinSkillDto(
    val id: Int,
    val title: String,
)