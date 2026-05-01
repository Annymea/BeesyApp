package com.beesy.ui.list

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

class ToDoListViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ListUiState())
    val uiState: StateFlow<ListUiState> = _uiState.asStateFlow()

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
            )
        }
    }
}