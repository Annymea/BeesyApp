package com.beesy.ui.list

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@Composable
fun AddItem(
    onClickAdd: () -> (Unit),
    modifier: Modifier = Modifier,
) {
    FloatingActionButton(
        modifier = modifier,
        onClick = { onClickAdd() },
    ) {
        Icon(Icons.Default.Add, "Floating action button.")
    }
}

@Composable
fun InputForNewItem(
    onConfirmAdding: (String) -> (Unit),
    startAdding: Boolean,
    modifier: Modifier = Modifier,
) {
    var value by rememberSaveable { mutableStateOf("") }

    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(startAdding) {
        focusRequester.requestFocus()
    }

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
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                ),
            modifier =
                Modifier
                    .weight(1f)
                    .focusRequester(focusRequester),
            value = value,
            onValueChange = { value = it },
            singleLine = true,
            placeholder = {
                Text(text = "Neue Aufgabe")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Create,
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
            colors =
                IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ),
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "create list item",
            )
        }
    }
}