package com.beesy.ui.skills

data class SkillListElement(
    val id: Int,
    val title: String,
    val experience: Int,
    val level: Int,
)

data class SkillsUiState(
    val shownSkills: List<SkillListElement> = listOf(),
    val adding: Boolean = false,
    val editedElement: SkillListElement? = null,
)