package com.beesy.ui.list

data class ListElement(
    val checked: Boolean,
    val title: String,
    val id: Int,
    val skills: List<String>,
)

enum class TaskState {
    IDLE,
    ADD,
    SORT,
}

data class ListUiState(
    val shownList: List<ListElement> = listOf(),
    val taskState: TaskState = TaskState.IDLE,
    val editItem: ListElement? = null,
)