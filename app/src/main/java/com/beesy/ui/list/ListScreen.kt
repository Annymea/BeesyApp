package com.beesy.ui.list

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.beesy.R

@Composable
fun ToDoScreen(
    modifier: Modifier = Modifier,
    listViewModel: ToDoListViewModel = viewModel(),
) {
    val uiState by listViewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { AppBar() },
    ) { innerPadding ->
        ToDoList(
            modifier = Modifier.padding(innerPadding),
            list = uiState.shownList,
            onCheckboxClicked = {listViewModel.onCheckboxChange(it)},
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(modifier: Modifier = Modifier) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(text = stringResource(R.string.app_name))
        },
    )
}

@Composable
fun ToDoList(
    onCheckboxClicked: (ListElement) -> (Unit),
    modifier: Modifier = Modifier,
    list: List<ListElement>,
) {
    LazyColumn(modifier = modifier) {
        items(list) { item ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
            )
            {
                Checkbox(
                    checked = item.checked,
                    onCheckedChange = { onCheckboxClicked(item) },
                )
                Text(text = item.title)
            }
        }
    }
}