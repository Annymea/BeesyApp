package com.beesy.ui.list

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.beesy.R

@Composable
fun AddItem(
    onAddItem: (String) -> (Unit),
    modifier: Modifier = Modifier,
) {
    val openAddDialog = remember { mutableStateOf(false) }

    FloatingActionButton(
        modifier = modifier,
        onClick = {
            openAddDialog.value = true
        },
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(R.string.add_task),
        )
    }

    when {
        openAddDialog.value -> {
            ItemDialog(
                title = stringResource(R.string.add_new_task),
                onSave = {
                    onAddItem(it)
                    openAddDialog.value = false
                },
                onDismiss = {
                    openAddDialog.value = false
                },
            )
        }
    }
}