package com.beesy.ui.list

import java.util.UUID

data class ListElement(
    val checked: Boolean,
    val title: String,
    val id: UUID,
)

data class ListUiState(
    val shownList: List<ListElement> =
        listOf(
            ListElement(checked = false, title = "Karteikarten", id = UUID.randomUUID()),
            ListElement(checked = false, title = "Yoga", id = UUID.randomUUID()),
            ListElement(checked = true, title = "Haushalt", id = UUID.randomUUID()),
        ),

    val adding: Boolean = false,
    val addedWord: String = "",
    val editing: ListElement? = null
)