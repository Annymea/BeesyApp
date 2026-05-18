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
            setState(
                state = State.IDLE,
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

    suspend fun deleteAllCheckedItems() {
        val allCheckedTasks = uiState.value.shownList.filter { it.checked }
        taskRepository.deleteMultipleTasks(
            allCheckedTasks.map {
                TaskDto(
                    id = it.id,
                    title = it.title,
                    checked = it.checked,
                    order = uiState.value.shownList.indexOf(it),
                )
            },
        )

        reloadAllTasks()
    }

    fun sortElementUp(element: ListElement) {
        _uiState.update { currentState ->
            val index = currentState.shownList.indexOf(element)

            if (index <= 0 || index >= currentState.shownList.size) return

            val tempList = currentState.shownList.toMutableList()
            val t = tempList[index]
            tempList[index] = tempList[index - 1]
            tempList[index - 1] = t

            currentState.copy(
                shownList = tempList,
            )
        }
    }

    fun sortElementDown(element: ListElement) {
        _uiState.update { currentState ->
            val index = currentState.shownList.indexOf(element)

            if (index < 0 || index >= currentState.shownList.size - 1) return

            val tempList = currentState.shownList.toMutableList()
            val t = tempList[index]
            tempList[index] = tempList[index + 1]
            tempList[index + 1] = t

            currentState.copy(
                shownList = tempList,
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
                order = _uiState.value.shownList.indexOf(toggleElement),
            ),
        )
        reloadAllTasks()
    }

    suspend fun addItem(newItem: String) {
        taskRepository.insertTask(
            TaskDto(
                title = newItem,
                checked = false,
                order = _uiState.value.shownList.lastIndex + 1,
            ),
        )
        reloadAllTasks()
        setState(
            state = State.IDLE,
        )
    }

    fun cancelEditing() {
        setState(
            state = State.IDLE,
        )
    }

    fun startAdding() {
        setState(
            state = State.ADD,
        )
    }

    fun startSorting() {
        setState(
            state = State.SORT,
        )
    }

    suspend fun finishSorting(save: Boolean) {
        if (save) {
            taskRepository.saveSorting(
                uiState.value.shownList.mapIndexed { index, item ->
                    TaskDto(
                        id = item.id,
                        title = item.title,
                        checked = item.checked,
                        order = index,
                    )
                },
            )
        }

        setState(
            state = State.IDLE,
        )
        reloadAllTasks()
    }

    suspend fun deleteItem(deleteItem: ListElement) {
        taskRepository.deleteTask(
            TaskDto(
                id = deleteItem.id,
                title = deleteItem.title,
                checked = deleteItem.checked,
                order = _uiState.value.shownList.indexOf(deleteItem),
            ),
        )
        reloadAllTasks()
    }

    suspend fun updateListItem(
        updateItem: ListElement,
        newName: String,
    ) {
        setState(
            state = State.IDLE,
        )

        updateTask(
            TaskDto(
                id = updateItem.id,
                title = newName,
                checked = updateItem.checked,
                order = _uiState.value.shownList.indexOf(updateItem),
            ),
        )

        reloadAllTasks()
    }

    fun startEditingItem(listItem: ListElement) {
        setState(
            edit = listItem,
            state = State.EDIT,
        )
    }

    private fun setState(
        edit: ListElement? = null,
        state: State,
    ) {
        _uiState.update { currentState ->
            currentState.copy(
                editItem = edit,
                state = state,
            )
        }
    }
}