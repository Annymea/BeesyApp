package com.beesy

import androidx.lifecycle.ViewModel

class ToDoListViewModel : ViewModel() {
    val toDos: List<String> = listOf("Karteikarten machen", "Haushalt", "Yoga")
}