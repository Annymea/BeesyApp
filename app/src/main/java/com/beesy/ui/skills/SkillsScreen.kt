package com.beesy.ui.skills

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.beesy.R
import com.beesy.ui.AppViewModelProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkillsScreen(
    modifier: Modifier = Modifier,
    navigationBar: @Composable (() -> Unit) = {},
    skillViewModel: SkillsViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val uiState by skillViewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = { navigationBar() },
        topBar = { SkillAppBar() },
        floatingActionButton = {
            if (!uiState.adding) AddSkillFAB(startAdding = { skillViewModel.startAdding() })
        },
    ) { innerPadding ->
        Box(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .consumeWindowInsets(innerPadding)
                    .imePadding(),
        ) {
            ShowAllSkills(
                isAdding = uiState.adding,
                skills = uiState.shownSkills,
                addSkill = {
                    skillViewModel.addSkill(it)
                },
            )
        }
    }
}

@Composable
fun ShowAllSkills(
    addSkill: (String) -> (Unit),
    isAdding: Boolean,
    skills: List<SkillListElement>,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberLazyListState()
    if (isAdding) {
        LaunchedEffect(true) {
            scrollState.animateScrollToItem(skills.size)
        }
    }

    LazyColumn(
        state = scrollState,
        modifier =
            Modifier
                .fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(skills) { item ->
            SkillListElement(skill = item)
        }
        if (isAdding) {
            item {
                AddSkillInput(onAddSkill = { addSkill(it) })
            }
        }
        if (!isAdding) {
            item {
                Spacer(modifier.height(52.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkillAppBar(modifier: Modifier = Modifier) {
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
}