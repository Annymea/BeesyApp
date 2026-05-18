package com.beesy.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.beesy.R
import com.beesy.ui.theme.MyApplicationTheme

@Composable
fun ShowList(
    onCheckboxClicked: (ListElement) -> (Unit),
    onDeleteClicked: (ListElement) -> (Unit),
    onEditClicked: (ListElement) -> (Unit),
    onFinishEditClicked: (ListElement, String) -> Unit,
    onConfirmAdding: (String) -> (Unit),
    onCancelEditClicked: () -> Unit,
    editedItem: ListElement?,
    adding: Boolean,
    list: List<ListElement>,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(adding) {
        listState.animateScrollToItem(list.size)
        focusRequester.requestFocus()
    }

    LazyColumn(
        state = listState,
        modifier =
            modifier
                .fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
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
            )
        }
        item {
            if (adding) {
                InputForNewItem(
                    onConfirmAdding = { onConfirmAdding(it) },
                    modifier = Modifier,
                    startAdding = true,
                )
            }
        }
        item {
            if (!adding) {
                Spacer(modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun TaskListElementInput(
    listElement: ListElement,
    startEditing: Boolean,
    onFinishEditClicked: (ListElement, String) -> Unit,
    onCancelEditClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var value by rememberSaveable(listElement.id, stateSaver = TextFieldValue.Saver) {
        mutableStateOf(
            TextFieldValue(listElement.title, TextRange(listElement.title.length)),
        )
    }

    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(startEditing) {
        focusRequester.requestFocus()
    }

    BasicTextField(
        textStyle =
            MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
        modifier =
            modifier
                .padding(14.dp)
                .focusRequester(focusRequester),
        value = value,
        onValueChange = { value = it },
        keyboardOptions =
            KeyboardOptions(
                imeAction = ImeAction.Done,
            ),
        keyboardActions =
            KeyboardActions(
                onDone = { onFinishEditClicked(listElement, value.text) },
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
                    ),
            imageVector = Icons.Filled.Clear,
            contentDescription = stringResource(R.string.cancel_editing_task),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Icon(
            modifier =
                Modifier
                    .clickable(
                        onClick = { onFinishEditClicked(listElement, value.text) },
                    ),
            imageVector = Icons.Filled.Check,
            contentDescription = stringResource(R.string.finish_editing_task),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
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
    listElement: ListElement,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier =
            modifier.background(
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = RoundedCornerShape(8.dp),
            ),
        verticalAlignment = Alignment.CenterVertically,
    )
    {
        if (edit) {
            TaskListElementInput(
                listElement = listElement,
                modifier = Modifier.weight(1f),
                onCancelEditClicked = { onCancelEditClicked() },
                onFinishEditClicked = { item, title ->
                    onFinishEditClicked(item, title)
                },
                startEditing = true,
            )
        }
        if (!edit) {
            Checkbox(
                checked = listElement.checked,
                onCheckedChange = { onCheckboxClicked(listElement) },
                colors =
                    CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.primaryContainer,
                        checkmarkColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    ),
            )
            Text(
                modifier = Modifier.weight(1f),
                text =
                    listElement.title.ifBlank {
                        stringResource(R.string.new_task)
                    },
            )
            Box(
                modifier =
                    Modifier
                        .padding(end = 16.dp),
            ) {
                Icon(
                    modifier =
                        Modifier
                            .clickable(onClick = { expanded = !expanded }),
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = stringResource(R.string.more_options),
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.edit)) },
                        onClick = {
                            onEditClicked(listElement)
                            expanded = false
                        },
                    )
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.delete)) },
                        onClick = {
                            onDeleteClicked(listElement)
                            expanded = false
                        },
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun ElementPreview2() {
    MyApplicationTheme {
        TaskListElement(
            onCheckboxClicked = { },
            onDeleteClicked = { },
            onEditClicked = { },
            onFinishEditClicked = { _, _ -> },
            onCancelEditClicked = { },
            edit = false,
            listElement = ListElement(id = 1, title = "Test", checked = true),
        )
    }
}

@Preview
@Composable
fun EditedElementPreview2() {
    MyApplicationTheme {
        TaskListElement(
            onCheckboxClicked = { },
            onDeleteClicked = { },
            onEditClicked = { },
            onFinishEditClicked = { _, _ -> },
            onCancelEditClicked = { },
            edit = true,
            listElement = ListElement(id = 1, title = "Test", checked = true),
        )
    }
}