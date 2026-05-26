package com.beesy.ui.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.beesy.R
import com.beesy.ui.theme.MyApplicationTheme

@Composable
fun ItemDialog(
    title: String,
    onDismiss: () -> (Unit),
    onSave: (String) -> (Unit),
    task: String = "",
) {
    var value by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(
            TextFieldValue(task, TextRange(task.length)),
        )
    }

    val focusRequester = remember { FocusRequester() }

    Dialog(
        onDismissRequest = { onDismiss() },
    ) {
        LaunchedEffect(true) {
            focusRequester.requestFocus()
        }

        Card(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 16.dp),
                    text = title,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                TextField(
                    shape = RoundedCornerShape(8.dp),
                    colors =
                        TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                            focusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        ),
                    modifier = Modifier.focusRequester(focusRequester),
                    value = value,
                    onValueChange = { value = it },
                    singleLine = true,
                    placeholder = { Text(text = stringResource(R.string.new_task)) },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { onSave(value.text) }),
                )

                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    Button(
                        modifier =
                            Modifier
                                .weight(1f)
                                .padding(start = 16.dp, end = 8.dp),
                        onClick = { onDismiss() },
                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            ),
                    ) {
                        Text(text = stringResource(R.string.dismiss))
                    }
                    Button(
                        modifier =
                            Modifier
                                .weight(1f)
                                .padding(start = 8.dp, end = 16.dp),
                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            ),
                        onClick = { onSave(value.text) },
                    ) {
                        Text(text = stringResource(R.string.save))
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PrevAddDialog() {
    MyApplicationTheme {
        ItemDialog(
            onDismiss = {},
            onSave = {},
            title = stringResource(R.string.add_new_task),
        )
    }
}

@Preview
@Composable
fun PrevEditDialog() {
    MyApplicationTheme {
        ItemDialog(
            onDismiss = {},
            onSave = {},
            title = "Edit task",
        )
    }
}