package com.beesy.ui.list

import android.util.Log
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
import java.util.UUID

class ToDoListViewModel(
    private val taskRepository: TaskRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ListUiState())
    val uiState: StateFlow<ListUiState> = _uiState.asStateFlow()

    private lateinit var tasks: List<Task>

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val task = Task(title = "Test", checked = false)
            insertTask(task)
            tasks = taskRepository.getAllTasks()
            Log.i("Datenbanktest", getAllTasks().toString())
        }
    }

    suspend fun insertTask(task: Task) {
        taskRepository.insertTask(task)
    }

    suspend fun getAllTasks(): List<Task> = taskRepository.getAllTasks()

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

    fun addItem(newItem: String) {
        _uiState.update { currentState ->
            currentState.copy(
                shownList =
                    currentState.shownList +
                        ListElement(
                            title =
                                newItem.ifBlank {
                                    "Neue Aufgabe"
                                },
                            checked = false,
                            id = UUID.randomUUID(),
                        ),
                adding = false,
            )
        }
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