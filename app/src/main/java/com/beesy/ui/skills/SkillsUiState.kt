package com.beesy.ui.skills

data class SkillListElement(
    val id: Int,
    val title: String,
    val experience: Int,
    val level: Int,
)

enum class SkillListState {
    IDLE,
    ADD,
    EDIT,
}

data class SkillsUiState(
    val skillsState: SkillListState = SkillListState.IDLE,
    val shownSkills: List<SkillListElement> = listOf(),
    val editedElement: SkillListElement? = null,
)