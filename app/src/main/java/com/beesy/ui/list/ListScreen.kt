package com.beesy.ui.list

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.beesy.ui.AppViewModelProvider
import com.beesy.ui.theme.MyApplicationTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ToDoScreen(
    modifier: Modifier = Modifier,
    listViewModel: ToDoListViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val uiState by listViewModel.uiState.collectAsState()
    val focusRequester = remember { FocusRequester() }
    val composableScope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            AppBar(
                onReorderClicked = { listViewModel.startSorting() },
                onSaveSorting = {
                    composableScope.launch(Dispatchers.IO) {
                        listViewModel.finishSorting(true)
                    }
                },
                onCancelSorting = {
                    composableScope.launch(Dispatchers.IO) {
                        listViewModel.finishSorting(false)
                    }
                },
                state = uiState.state,
                onDeleteAllCheckedClicked = {
                    composableScope.launch(Dispatchers.IO) {
                        listViewModel.deleteAllCheckedItems()
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .padding(innerPadding),
        ) {
            if (uiState.state != State.SORT) {
                ShowList(
                    modifier = Modifier.weight(1f),
                    list = uiState.shownList,
                    onCheckboxClicked = {
                        composableScope.launch(Dispatchers.IO) {
                            listViewModel.onCheckboxChange(it)
                        }
                    },
                    onConfirmAdding = {
                        composableScope.launch(Dispatchers.IO) {
                            listViewModel.addItem(it)
                        }
                    },
                    onDeleteClicked = {
                        composableScope.launch(Dispatchers.IO) {
                            listViewModel.deleteItem(it)
                        }
                    },
                    adding = uiState.state == State.ADD,
                    addTaskKeyboardFocus = focusRequester,
                    onFinishEditClicked = { item, name ->
                        composableScope.launch(Dispatchers.IO) {
                            listViewModel.updateListItem(item, name)
                        }
                    },
                    onEditClicked = { listViewModel.startEditingItem(it) },
                    onCancelEditClicked = { listViewModel.cancelEditing() },
                    editedItem = uiState.editItem,
                )
                if (uiState.state != State.ADD) {
                    Surface(
                        tonalElevation = 1.dp,
                        shadowElevation = 8.dp,
                    ) {
                        Row(
                            modifier =
                                Modifier
                                    .padding(16.dp),
                        ) {
                            Spacer(
                                modifier = Modifier.weight(1f),
                            )
                            AddItem(
                                modifier = Modifier.size(52.dp),
                                onClickAdd = {
                                    listViewModel.startAdding()
                                },
                            )
                        }
                    }
                }
            }
            if (uiState.state == State.SORT) {
                ReorderList(
                    onSortElementUp = { listViewModel.sortElementUp(it) },
                    onSortElementDown = { listViewModel.sortElementDown(it) },
                    list = uiState.shownList,
                )
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ScreenPreview() {
    MyApplicationTheme {
        ToDoScreen()
    }
}

@Preview
@Composable
fun InputPreview() {
    MyApplicationTheme {
        InputForNewItem(
            onConfirmAdding = {},
            addTaskKeyboardFocus = remember { FocusRequester() },
        )
    }
}

@Preview
@Composable
fun ElementPreview() {
    MyApplicationTheme {
        TaskListElement(
            onCheckboxClicked = { },
            onDeleteClicked = { },
            onEditClicked = { },
            onFinishEditClicked = { _, _ -> },
            onCancelEditClicked = { },
            edit = false,
            focusRequester = remember { FocusRequester() },
            listElement = ListElement(id = 1, title = "Test", checked = true),
        )
    }
}

@Preview
@Composable
fun EditedElementPreview() {
    MyApplicationTheme {
        TaskListElement(
            onCheckboxClicked = { },
            onDeleteClicked = { },
            onEditClicked = { },
            onFinishEditClicked = { _, _ -> },
            onCancelEditClicked = { },
            edit = true,
            focusRequester = remember { FocusRequester() },
            listElement = ListElement(id = 1, title = "Test", checked = true),
        )
    }
}

@Preview
@Composable
fun ReorderElementPreview() {
    MyApplicationTheme {
        ReorderListElement(
            element = ListElement(id = 1, title = "Test", checked = true),
            onSortUpClicked = {},
            onSortDownClicked = {},
        )
    }
}