package com.beesy.ui.skills

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beesy.data.TaskRepository
import com.beesy.data.dto.SkillDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SkillsViewModel(
    private val taskRepository: TaskRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SkillsUiState())
    val uiState: StateFlow<SkillsUiState> = _uiState.asStateFlow()

    init {
        loadAllSkills()
    }

    fun loadAllSkills() {
        stopAdding()
        viewModelScope.launch(Dispatchers.IO) {
            val allSkills = taskRepository.getAllSkills()
            _uiState.update { currentState ->
                currentState.copy(
                    skillsState = SkillListState.IDLE,
                    shownSkills =
                        allSkills.map {
                            SkillListElement(
                                id = it.id,
                                title = it.title,
                                level = it.level,
                                experience = it.experience,
                            )
                        },
                )
            }
        }
    }

    fun editSkill(
        skill: SkillListElement,
        newName: String,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.updateSkill(
                SkillDto(
                    title = newName,
                    id = skill.id,
                    level = skill.level,
                    experience = skill.experience,
                ),
            )
            loadAllSkills()
            _uiState.update { currentState ->
                currentState.copy(
                    skillsState = SkillListState.IDLE,
                    editedElement = null,
                )
            }
        }
    }

    fun startEditSkill(skill: SkillListElement) {
        _uiState.update { currentState ->
            currentState.copy(
                skillsState = SkillListState.EDIT,
                editedElement = skill,
            )
        }
    }

    fun cancelEditing() {
        _uiState.update { currentState ->
            currentState.copy(
                skillsState = SkillListState.IDLE,
                editedElement = null,
            )
        }
    }

    fun stopAdding() {
        _uiState.update { currentState ->
            currentState.copy(
                skillsState = SkillListState.IDLE,
            )
        }
    }

    fun startAdding() {
        _uiState.update { currentState ->
            currentState.copy(
                skillsState = SkillListState.ADD,
                editedElement = null,
            )
        }
    }

    fun addSkill(title: String) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.addSkill(SkillDto(title = title))
            loadAllSkills()
            stopAdding()
        }
    }

    fun deleteSkill(skill: SkillListElement) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.deleteSkill(
                SkillDto(
                    title = skill.title,
                    id = skill.id,
                    experience = skill.experience,
                    level = skill.level,
                ),
            )
            loadAllSkills()
        }
    }
}