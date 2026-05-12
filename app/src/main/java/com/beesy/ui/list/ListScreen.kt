package com.beesy.ui.list

import android.content.res.Configuration
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.beesy.R
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
        topBar = { AppBar() },
        floatingActionButton = {
            if (!uiState.adding) {
                AddItem(
                    onClickAdd = {
                        listViewModel.startAdding()
                    },
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End,
    ) { innerPadding ->
        ToDoList(
            modifier = Modifier.padding(innerPadding),
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
            adding = uiState.adding,
            addTaskKeyboardFocus = focusRequester,
            onFinishEditClicked = { item, name ->
                composableScope.launch(Dispatchers.IO) {
                    listViewModel.updateListItem(item, name)
                }
            },
            onEditClicked = { listViewModel.startEditingItem(it) },
            onCancelEditClicked = { listViewModel.cancelEditing() },
            editedItem = uiState.editing,
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
    onEditClicked: (ListElement) -> (Unit),
    onFinishEditClicked: (ListElement, String) -> Unit,
    onConfirmAdding: (String) -> (Unit),
    onCancelEditClicked: () -> Unit,
    editedItem: ListElement?,
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
                    onEditClicked = { onEditClicked(it) },
                    onFinishEditClicked = { item, name ->
                        onFinishEditClicked(item, name)
                    },
                    onCancelEditClicked = { onCancelEditClicked() },
                    edit = editedItem?.id == item.id,
                    focusRequester = addTaskKeyboardFocus,
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
    onCheckboxClicked: (ListElement) -> Unit,
    onDeleteClicked: (ListElement) -> Unit,
    onEditClicked: (ListElement) -> Unit,
    onFinishEditClicked: (ListElement, String) -> (Unit),
    onCancelEditClicked: () -> (Unit),
    edit: Boolean,
    focusRequester: FocusRequester,
    listElement: ListElement,
    modifier: Modifier = Modifier,
) {
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
                ).background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(8.dp),
                ),
        verticalAlignment = Alignment.CenterVertically,
    )
    {
        if (edit) {
            // Mit Absicht hier positioniert, da ich die variable nur erstellen will, wenn das
            // Item editiert wird
            var value by rememberSaveable { mutableStateOf(listElement.title) }

            BasicTextField(
                textStyle =
                    MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                modifier =
                    Modifier
                        .weight(1f)
                        .padding(16.dp)
                        .focusRequester(focusRequester),
                // Ich kann hier nicht einfach sagen, dass ich den Title nehme,
                // weil ich den ja noch abbrechen will den Vorgang
                value = value,
                onValueChange = { value = it },
                keyboardOptions =
                    KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions =
                    KeyboardActions(
                        onDone = { onFinishEditClicked(listElement, value) },
                    ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurfaceVariant),
            )
            Row(
                modifier = Modifier.padding(8.dp),
            ) {
                Icon(
                    modifier =
                        Modifier
                            .clickable(
                                onClick = { onCancelEditClicked() },
                            ).padding(8.dp),
                    imageVector = Icons.Filled.Clear,
                    contentDescription = "Cancel editing task",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Icon(
                    modifier =
                        Modifier
                            .clickable(
                                onClick = { onFinishEditClicked(listElement, value) },
                            ).padding(8.dp),
                    imageVector = Icons.Filled.Check,
                    contentDescription = "Finish editing task",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
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
    var value by rememberSaveable { mutableStateOf("") }

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
            value = value,
            onValueChange = { value = it },
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
                    onDone = { onConfirmAdding(value) },
                ),
        )
        FilledIconButton(
            modifier =
                Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f),
            onClick = { onConfirmAdding(value) },
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