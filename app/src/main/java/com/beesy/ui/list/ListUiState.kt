package com.beesy.ui.list

data class ListElement(
    val checked: Boolean,
    val title: String,
    val id: Int,
)

enum class State {
    IDLE,
    ADD,
    EDIT,
    SORT,
}

data class ListUiState(
    val shownList: List<ListElement> = listOf(),
    val state: State = State.IDLE,
    val editItem: ListElement? = null,
)