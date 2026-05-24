package com.beesy.ui.list

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
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
    navigationBar: @Composable (() -> Unit) = {},
    listViewModel: ToDoListViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val uiState by listViewModel.uiState.collectAsState()
    val composableScope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            navigationBar()
        },
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
        floatingActionButton =
            {
                if (uiState.state != State.ADD && uiState.state != State.SORT) {
                    AddItem(
                        modifier = Modifier.size(52.dp),
                        onAddItem = {
                            composableScope.launch(Dispatchers.IO) {
                                listViewModel.addItem(it)
                            }
                        },
                    )
                }
            },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .consumeWindowInsets(innerPadding)
                    .imePadding(),
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
                    onDeleteClicked = {
                        composableScope.launch(Dispatchers.IO) {
                            listViewModel.deleteItem(it)
                        }
                    },
                    onFinishEditClicked = { item, name ->
                        composableScope.launch(Dispatchers.IO) {
                            listViewModel.updateListItem(item, name)
                        }
                    },
                )
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
fun ElementPreview() {
    MyApplicationTheme {
        TaskListElement(
            onCheckboxClicked = { },
            onDeleteClicked = { },
            onFinishEditClicked = { _, _ -> },
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
            onFinishEditClicked = { _, _ -> },
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