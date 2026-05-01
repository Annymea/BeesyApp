package com.beesy.ui.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.beesy.R
import com.beesy.ui.theme.MyApplicationTheme

@Composable
fun ToDoScreen(
    modifier: Modifier = Modifier,
    listViewModel: ToDoListViewModel = viewModel(),
) {
    val uiState by listViewModel.uiState.collectAsState()
    var adding by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { AppBar() },
        floatingActionButton = {
            if (!adding) {
                AddItem(
                    onClickAdd = {
                        adding = true
                    },
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End,
    ) { innerPadding ->
        ToDoList(
            modifier = Modifier.padding(innerPadding),
            list = uiState.shownList,
            onCheckboxClicked = { listViewModel.onCheckboxChange(it) },
            onConfirmAdding = {
                adding = false
                listViewModel.addItem(it)
            },
            adding = adding,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(modifier: Modifier = Modifier) {
    TopAppBar(
        colors =
            TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
            ),
        modifier = modifier,
        title = {
            Text(
                text = stringResource(R.string.app_name),
                color = MaterialTheme.colorScheme.onPrimary,
            )
        },
    )
}

@Composable
fun ToDoList(
    onCheckboxClicked: (ListElement) -> (Unit),
    onConfirmAdding: (String) -> (Unit),
    adding: Boolean,
    modifier: Modifier = Modifier,
    list: List<ListElement>,
) {
    val listState = rememberLazyListState()

    Column(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .weight(1f),
        ) {
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

        // only starts this, when adding an item
        LaunchedEffect(adding) {
            listState.animateScrollToItem(list.size)
        }
        if (adding) {
            InputForNewItem(
                onConfirmAdding = { onConfirmAdding(it) },
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}

@Composable
fun AddItem(
    onClickAdd: () -> (Unit),
    modifier: Modifier = Modifier,
) {
    FloatingActionButton(
        modifier = modifier,
        onClick = { onClickAdd() },
    ) {
        Icon(Icons.Filled.Add, "Floating action button.")
    }
}

@Composable
fun InputForNewItem(
    onConfirmAdding: (String) -> (Unit),
    modifier: Modifier = Modifier,
) {
    var state by rememberSaveable { mutableStateOf("") }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextField(
            label = {
                Text(text = "neue Aufgabe")
            },
            modifier = Modifier.weight(1f),
            value = state,
            onValueChange = { state = it },
            singleLine = true,
        )
        FilledIconButton(
            modifier = Modifier.padding(start = 8.dp),
            onClick = { onConfirmAdding(state) },
        ) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = "create list item",
            )
        }
    }
}

@Preview
@Composable
fun ScreenPreview() {
    MyApplicationTheme {
        ToDoScreen()
    }
}