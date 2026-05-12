package com.beesy.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beesy.data.Task
import com.beesy.data.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ToDoListViewModel(
    private val taskRepository: TaskRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ListUiState())
    val uiState: StateFlow<ListUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            reloadAllTasks()
        }
    }

    suspend fun reloadAllTasks() {
        val allTasks = taskRepository.getAllTasks()
        _uiState.update { currentState ->
            currentState.copy(
                shownList =
                    allTasks.map {
                        ListElement(
                            id = it.id,
                            title = it.title,
                            checked = it.checked,
                        )
                    },
            )
        }
    }

    fun onCheckboxChange(toggleElement: ListElement) {
        _uiState.update { currentState ->
            currentState.copy(
                shownList =
                    currentState.shownList.map {
                        if (it.id == toggleElement.id) {
                            it.copy(checked = !it.checked)
                        } else {
                            it
                        }
                    },
            )
        }
    }

    suspend fun addItem(newItem: String) {
        taskRepository.insertTask(
            Task(
                title =
                    newItem.ifBlank {
                        "Neue Aufgabe"
                    },
                checked = false,
            ),
        )

        reloadAllTasks()
    }

    fun cancelEditing() {
        _uiState.update { currentState ->
            currentState.copy(
                editing = null,
            )
        }
    }

    fun startAdding() {
        _uiState.update { currentState ->
            currentState.copy(
                adding = true,
                editing = null,
            )
        }
    }

    fun deleteItem(deleteItem: ListElement) {
        _uiState.update { currentState ->
            currentState.copy(
                shownList = currentState.shownList - deleteItem,
            )
        }
    }

    fun updateListItem(
        updateItem: ListElement,
        newName: String,
    ) {
        _uiState.update { currentState ->
            currentState.copy(
                shownList =
                    currentState.shownList.map {
                        if (it.id == updateItem.id) {
                            it.copy(title = newName)
                        } else {
                            it
                        }
                    },
                editing = null,
            )
        }
    }

    fun startEditingItem(listItem: ListElement) {
        _uiState.update { currentState ->
            currentState.copy(
                editing = listItem,
                adding = false,
            )
        }
    }
}