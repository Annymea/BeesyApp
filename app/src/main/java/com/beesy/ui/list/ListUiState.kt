package com.beesy.ui.list

data class ListElement(
    val checked: Boolean,
    val title: String,
    val id: Int,
)

data class ListUiState(
    val shownList: List<ListElement> = listOf(),
    val adding: Boolean = false,
    val editing: ListElement? = null,
)