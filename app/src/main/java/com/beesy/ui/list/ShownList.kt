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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.beesy.R
import com.beesy.ui.theme.MyApplicationTheme

@Composable
fun ShowList(
    onCheckboxClicked: (ListElement) -> (Unit),
    onDeleteClicked: (ListElement) -> (Unit),
    onFinishEditClicked: (ListElement, String) -> Unit,
    list: List<ListElement>,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()

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
                onFinishEditClicked = { item, name ->
                    onFinishEditClicked(item, name)
                },
            )
        }
        item {
            Spacer(modifier.height(40.dp))
        }
    }
}

@Composable
fun TaskListElement(
    onCheckboxClicked: (ListElement) -> Unit,
    onDeleteClicked: (ListElement) -> Unit,
    onFinishEditClicked: (ListElement, String) -> (Unit),
    listElement: ListElement,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    val openEditDialog = remember { mutableStateOf(false) }

    when {
        openEditDialog.value -> {
            ItemDialog(
                task = listElement.title,
                title = stringResource(R.string.edit_task),
                onSave = {
                    onFinishEditClicked(listElement, it)
                    openEditDialog.value = false
                },
                onDismiss = {
                    openEditDialog.value = false
                },
            )
        }
    }

    Row(
        modifier =
            modifier
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainer,
                    shape = RoundedCornerShape(8.dp),
                ).clickable(
                    onClick = {
                        onCheckboxClicked(listElement)
                    },
                ),
        verticalAlignment = Alignment.CenterVertically,
    )
    {
        Checkbox(
            checked = listElement.checked,
            onCheckedChange = { onCheckboxClicked(listElement) },
            colors =
                CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primaryContainer,
                    checkmarkColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    uncheckedColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
        )
        Text(
            color = MaterialTheme.colorScheme.onSurfaceVariant,
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
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                DropdownMenuItem(
                    text = {
                        Text(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            text = stringResource(R.string.edit),
                        )
                    },
                    onClick = {
                        openEditDialog.value = true
                        expanded = false
                    },
                )
                DropdownMenuItem(
                    text = {
                        Text(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            text = stringResource(R.string.delete),
                        )
                    },
                    onClick = {
                        onDeleteClicked(listElement)
                        expanded = false
                    },
                )
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
            onFinishEditClicked = { _, _ -> },
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
            onFinishEditClicked = { _, _ -> },
            listElement = ListElement(id = 1, title = "Test", checked = true),
        )
    }
}