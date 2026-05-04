package com.beesy.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
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
    val focusRequester = remember { FocusRequester() }

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
            onDeleteClicked = { listViewModel.deleteItem(it) },
            adding = adding,
            addTaskKeyboardFocus = focusRequester,
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
    onDeleteClicked: (ListElement) -> (Unit),
    onConfirmAdding: (String) -> (Unit),
    addTaskKeyboardFocus: FocusRequester,
    adding: Boolean,
    list: List<ListElement>,
    modifier: Modifier = Modifier,
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
                TaskListElement(
                    listElement = item,
                    onCheckboxClicked = { onCheckboxClicked(it) },
                    onDeleteClicked = { onDeleteClicked(it) },
                    onEditClicked = {},
                )
            }
            item {
                if (adding) {
                    InputForNewItem(
                        onConfirmAdding = { onConfirmAdding(it) },
                        modifier = Modifier.padding(16.dp),
                        addTaskKeyboardFocus = addTaskKeyboardFocus,
                    )
                }
            }
        }

        // only starts this, when adding an item
        LaunchedEffect(adding) {
            listState.animateScrollToItem(list.size)
            addTaskKeyboardFocus.requestFocus()
        }
    }
}

@Composable
fun TaskListElement(
    listElement: ListElement,
    onCheckboxClicked: (ListElement) -> Unit,
    onDeleteClicked: (ListElement) -> Unit,
    onEditClicked: (ListElement) -> Unit,
    modifier: Modifier = Modifier,
) {
    var state by rememberSaveable { mutableStateOf(listElement.title) }
    var edit by rememberSaveable { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(edit) {
        focusRequester.requestFocus()
    }

    Row(
        modifier =
            modifier
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 8.dp,
                    // bottom = 4.dp
                ).background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(8.dp),
                ),
        // .height(IntrinsicSize.Max),
        verticalAlignment = Alignment.CenterVertically,
    )
    {
        if (edit) {
            BasicTextField(
                textStyle = MaterialTheme.typography.bodyLarge,
                modifier =
                    Modifier
                        .weight(1f)
                        .padding(16.dp)
                        .focusRequester(focusRequester),
                value = state,
                onValueChange = { state = it },
                keyboardOptions =
                    KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions =
                    KeyboardActions(
                        onDone = { edit = false },
                    ),
            )
            Icon(
                modifier =
                    Modifier
                        .clickable(
                            onClick = { edit = false },
                        ).padding(8.dp),
                imageVector = Icons.Filled.Check,
                contentDescription = "Finish editing task",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        if (!edit) {
            Checkbox(
                checked = listElement.checked,
                onCheckedChange = { onCheckboxClicked(listElement) },
            )
            Text(
                modifier = Modifier.weight(1f),
                text = listElement.title,
            )
            Row(
                modifier = Modifier.padding(8.dp),
            ) {
                Icon(
                    modifier =
                        Modifier.clickable(
                            onClick = {
                                edit = true
                                onEditClicked(listElement)
                            },
                        ),
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Edit Task",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Icon(
                    modifier =
                        Modifier.clickable(
                            onClick = { onDeleteClicked(listElement) },
                        ),
                    imageVector = Icons.Filled.Clear,
                    contentDescription = "Delete Task",
                    tint = MaterialTheme.colorScheme.error,
                )
            }
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
    addTaskKeyboardFocus: FocusRequester,
    modifier: Modifier = Modifier,
) {
    var state by rememberSaveable { mutableStateOf("") }

    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextField(
            shape =
                RoundedCornerShape(
                    topStart = 8.dp,
                    bottomStart = 8.dp,
                ),
            colors =
                TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                ),
            modifier =
                Modifier
                    .weight(1f)
                    .focusRequester(addTaskKeyboardFocus),
            value = state,
            onValueChange = { state = it },
            singleLine = true,
            placeholder = {
                Text(text = "Neue Aufgabe")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Create,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            },
            keyboardOptions =
                KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions =
                KeyboardActions(
                    onDone = { onConfirmAdding(state) },
                ),
            trailingIcon = {
            },
        )
        FilledIconButton(
            modifier =
                Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f),
            onClick = { onConfirmAdding(state) },
            shape =
                RoundedCornerShape(
                    topEnd = 8.dp,
                    bottomEnd = 8.dp,
                ),
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