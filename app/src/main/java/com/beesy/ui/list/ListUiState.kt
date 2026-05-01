package com.beesy.ui.list

data class ListElement(
    val checked: Boolean,
    val title: String,
)

data class ListUiState(
    val shownList: List<ListElement> =
        listOf(
            ListElement(checked = false, title = "Karteikarten"),
            ListElement(checked = false, title = "Yoga"),
            ListElement(checked = true, title = "Haushalt"),
        ),
)