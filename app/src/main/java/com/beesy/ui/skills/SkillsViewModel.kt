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

    fun stopAdding() {
        _uiState.update { currentState ->
            currentState.copy(
                adding = false,
            )
        }
    }

    fun startAdding() {
        _uiState.update { currentState ->
            currentState.copy(
                adding = true,
            )
        }
    }

    fun addSkill(title: String) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.addSkill(SkillDto(title = title))
            loadAllSkills()
        }
    }
}