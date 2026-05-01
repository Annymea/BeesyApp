package com.beesy.ui.list

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ToDoListViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ListUiState())
    val uiState: StateFlow<ListUiState> = _uiState.asStateFlow()

    fun onCheckboxChange(toggleElement: ListElement) {
        _uiState.update { currentState ->
            val indexToggleElement = currentState.shownList.indexOf(toggleElement)

            currentState.copy(
                shownList =
                    currentState.shownList.mapIndexed { index, element ->
                        if (index == indexToggleElement) {
                            element.copy(checked = !element.checked)
                        } else {
                            element
                        }
                    },
            )
        }
    }
}