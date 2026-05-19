package com.beesy.ui.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.beesy.R
import com.beesy.ui.theme.MyApplicationTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    onSaveSorting: () -> Unit,
    onCancelSorting: () -> Unit,
    onReorderClicked: () -> Unit,
    onDeleteAllCheckedClicked: () -> Unit,
    state: State,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        colors =
            TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            ),
        modifier = modifier,
        title = {
            Text(
                text = stringResource(R.string.app_name),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        },
        actions = {
            if (state == State.SORT) {
                ButtonsForEndSorting(
                    onSaveSorting = { onSaveSorting() },
                    onCancelSorting = { onCancelSorting() },
                )
            } else {
                AppBarMenu(
                    onReorderClicked = { onReorderClicked() },
                    onDeleteAllCheckedClicked = { onDeleteAllCheckedClicked() },
                )
            }
        },
    )
}

@Composable
fun ButtonsForEndSorting(
    onSaveSorting: () -> (Unit),
    onCancelSorting: () -> (Unit),
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        Button(
            modifier = Modifier.padding(end = 8.dp),
            onClick = {
                onCancelSorting()
            },
            colors =
                ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    contentColor = MaterialTheme.colorScheme.primaryContainer,
                ),
        ) {
            Text(
                text = stringResource(R.string.cancel),
            )
        }

        Button(
            modifier = Modifier.padding(end = 8.dp),
            onClick = {
                onSaveSorting()
            },
            colors =
                ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    contentColor = MaterialTheme.colorScheme.primaryContainer,
                ),
        ) {
            Text(text = stringResource(R.string.save))
        }
    }
}

@Composable
fun AppBarMenu(
    onReorderClicked: () -> Unit,
    onDeleteAllCheckedClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier =
            modifier
                .padding(16.dp),
    ) {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = stringResource(R.string.more_options),
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(R.string.sort_list),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                },
                onClick = { onReorderClicked() },
            )
            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(R.string.delete_finished_tasks),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                },
                onClick = { onDeleteAllCheckedClicked() },
            )
        }
    }
}

@Preview
@Composable
fun TopBarPreviewNoEdit() {
    MyApplicationTheme {
        AppBar(
            onSaveSorting = { },
            onCancelSorting = { },
            onReorderClicked = { },
            onDeleteAllCheckedClicked = {},
            state = State.IDLE,
        )
    }
}

@Preview
@Composable
fun TopBarPreviewEdit() {
    MyApplicationTheme {
        AppBar(
            onDeleteAllCheckedClicked = {},
            onSaveSorting = { },
            onCancelSorting = { },
            onReorderClicked = { },
            state = State.SORT,
        )
    }
}