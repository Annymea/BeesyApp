package com.beesy.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beesy.data.TaskRepository
import com.beesy.data.dto.TaskDto
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
            setEditAndAdd(
                edit = null,
                add = false,
            )
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

    suspend fun updateTask(task: TaskDto) {
        taskRepository.updateItem(task)
    }

    suspend fun onCheckboxChange(toggleElement: ListElement) {
        updateTask(
            TaskDto(
                id = toggleElement.id,
                title = toggleElement.title,
                checked = !toggleElement.checked,
            ),
        )
        reloadAllTasks()
    }

    suspend fun addItem(newItem: String) {
        taskRepository.insertTask(
            TaskDto(
                title =
                    newItem.ifBlank {
                        "Neue Aufgabe"
                    },
                checked = false,
            ),
        )
        reloadAllTasks()
        setEditAndAdd(
            edit = null,
            add = false,
        )
    }

    fun cancelEditing() {
        setEditAndAdd(
            edit = null,
            add = false,
        )
    }

    fun startAdding() {
        setEditAndAdd(
            edit = null,
            add = true,
        )
    }

    suspend fun deleteItem(deleteItem: ListElement) {
        taskRepository.deleteTask(
            TaskDto(
                id = deleteItem.id,
                title = deleteItem.title,
                checked = deleteItem.checked,
            ),
        )
        reloadAllTasks()
    }

    suspend fun updateListItem(
        updateItem: ListElement,
        newName: String,
    ) {
        setEditAndAdd(
            edit = null,
            add = false,
        )

        updateTask(
            TaskDto(
                id = updateItem.id,
                title = newName,
                checked = updateItem.checked,
            ),
        )

        reloadAllTasks()
    }

    fun startEditingItem(listItem: ListElement) {
        setEditAndAdd(
            edit = listItem,
            add = false,
        )
    }

    private fun setEditAndAdd(
        edit: ListElement?,
        add: Boolean,
    ) {
        _uiState.update { currentState ->
            currentState.copy(
                editing = edit,
                adding = add,
            )
        }
    }
}