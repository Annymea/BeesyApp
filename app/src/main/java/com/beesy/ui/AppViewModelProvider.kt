package com.beesy.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.beesy.BeesyApplication
import com.beesy.ui.list.ToDoListViewModel

object AppViewModelProvider {
    val Factory =
        viewModelFactory {
            initializer {
                ToDoListViewModel(BeesyApplication().container.taskRepository)
            }
        }
}

fun CreationExtras.BeesyApplication(): BeesyApplication =
    (
        this[AndroidViewModelFactory.APPLICATION_KEY] as BeesyApplication
    )