package com.beesy.ui.skills

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.beesy.R
import com.beesy.ui.theme.MyApplicationTheme

@Composable
fun SkillListElement(
    edit: Boolean,
    onFinishEditClicked: (SkillListElement, String) -> Unit,
    onCancelEditClicked: () -> Unit,
    onStartEditSkill: (SkillListElement) -> (Unit),
    onDeleteSkill: (SkillListElement) -> (Unit),
    skill: SkillListElement,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    val openEditDialog = remember { mutableStateOf(false) }

    Column(
        modifier =
            modifier
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainer,
                    shape = RoundedCornerShape(8.dp),
                ).fillMaxWidth()
                .padding(16.dp),
    ) {
        if (edit) {
            SkillListElementInput(
                skillListElement = skill,
                onFinishEditClicked = { skill, name -> onFinishEditClicked(skill, name) },
                onCancelEditClicked = { onCancelEditClicked() },
            )
        }
        if (!edit) {
            Row(modifier = Modifier.padding(bottom = 16.dp)) {
                Text(
                    modifier =
                        modifier
                            .padding(end = 8.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = RoundedCornerShape(8.dp),
                            ).width(32.dp),
                    text = skill.level.toString(),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    textAlign = TextAlign.Center,
                )

                Text(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f),
                    text =
                        skill.title.ifBlank {
                            stringResource(R.string.new_skill)
                        },
                )

                Box {
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
                                onStartEditSkill(skill)
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
                                onDeleteSkill(skill)
                                expanded = false
                            },
                        )
                    }
                }
            }

            LinearProgressIndicator(
                progress = { skill.experience.toFloat() / 1000 },
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(15.dp),
                color = MaterialTheme.colorScheme.primaryContainer,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                drawStopIndicator = {},
            )
        }
    }
}

@Composable
fun SkillListElementInput(
    skillListElement: SkillListElement,
    onFinishEditClicked: (SkillListElement, String) -> Unit,
    onCancelEditClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var value by rememberSaveable(skillListElement.id, stateSaver = TextFieldValue.Saver) {
        mutableStateOf(
            TextFieldValue(skillListElement.title, TextRange(skillListElement.title.length)),
        )
    }

    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(true) {
        focusRequester.requestFocus()
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BasicTextField(
            textStyle =
                MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
            modifier =
                modifier
                    .padding(14.dp)
                    .focusRequester(focusRequester)
                    .weight(1f),
            value = value,
            onValueChange = { value = it },
            keyboardOptions =
                KeyboardOptions(
                    imeAction = ImeAction.Done,
                ),
            keyboardActions =
                KeyboardActions(
                    onDone = { onFinishEditClicked(skillListElement, value.text) },
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
                            onClick = { onFinishEditClicked(skillListElement, value.text) },
                        ),
                imageVector = Icons.Filled.Check,
                contentDescription = stringResource(R.string.finish_editing_task),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Preview
@Composable
fun PrevSkillElement() {
    MyApplicationTheme {
        SkillListElement(
            edit = false,
            onDeleteSkill = {},
            onFinishEditClicked = { _, _ -> },
            onStartEditSkill = {},
            onCancelEditClicked = {},
            skill =
                SkillListElement(
                    id = 1,
                    title = "TestSkill",
                    experience = 300,
                    level = 9,
                ),
        )
    }
}

@Preview
@Composable
fun PrevEditSkillElement() {
    MyApplicationTheme {
        SkillListElement(
            edit = true,
            onDeleteSkill = {},
            onFinishEditClicked = { _, _ -> },
            onStartEditSkill = {},
            onCancelEditClicked = {},
            skill =
                SkillListElement(
                    id = 1,
                    title = "TestSkill",
                    experience = 300,
                    level = 9,
                ),
        )
    }
}