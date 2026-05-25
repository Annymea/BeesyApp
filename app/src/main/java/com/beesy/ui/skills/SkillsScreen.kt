package com.beesy.ui.skills

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.beesy.R
import com.beesy.ui.AppViewModelProvider
import com.beesy.ui.theme.MyApplicationTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkillsScreen(
    modifier: Modifier = Modifier,
    navigationBar: @Composable (() -> Unit) = {},
    skillViewModel: SkillsViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val uiState by skillViewModel.uiState.collectAsState()
    val scrollState = rememberLazyListState()

    if (uiState.adding) {
        LaunchedEffect(true) {
            scrollState.animateScrollToItem(uiState.shownSkills.size)
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            navigationBar()
        },
        topBar = {
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
            )
        },
        floatingActionButton = {
            if (!uiState.adding) {
                FloatingActionButton(
                    onClick = { skillViewModel.startAdding() },
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add skill",
                    )
                }
            }
        },
    ) { innerPadding ->
        Box(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .consumeWindowInsets(innerPadding)
                    .imePadding(),
        ) {
            LazyColumn(
                state = scrollState,
                modifier =
                    Modifier
                        .fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(uiState.shownSkills) { item ->
                    SkillListElement(skill = item)
                }
                if (uiState.adding) {
                    item {
                        AddSkillInput(
                            onAddSkill = {
                                skillViewModel.addSkill(it)
                                skillViewModel.stopAdding()
                            },
                        )
                    }
                }
                if (!uiState.adding) {
                    item {
                        Spacer(modifier.height(52.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun SkillListElement(
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
                ).padding(16.dp),
    ) {
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
                            expanded = false
                        },
                    )
                }
            }
        }

        LinearProgressIndicator(
            progress = { skill.experience.toFloat() / 1000 },
            modifier = Modifier.fillMaxWidth().height(15.dp),
            color =
                ProgressIndicatorDefaults.linearTrackColor(),
        )
    }
}

@Composable
fun AddSkillInput(
    onAddSkill: (String) -> (Unit),
    modifier: Modifier = Modifier,
) {
    var value by rememberSaveable { mutableStateOf("") }

    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(true) {
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
                Text(text = stringResource(R.string.new_skill))
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
                    onDone = { onAddSkill(value) },
                ),
        )
        FilledIconButton(
            modifier =
                Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f),
            onClick = { onAddSkill(value) },
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
                contentDescription = stringResource(R.string.add_task),
            )
        }
    }
}

@Preview
@Composable
fun PrevSkillElement() {
    MyApplicationTheme {
        SkillListElement(
            SkillListElement(
                id = 1,
                title = "TestSkill",
                experience = 300,
                level = 9,
            ),
        )
    }
}