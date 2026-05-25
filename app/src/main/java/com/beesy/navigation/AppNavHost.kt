package com.beesy.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.beesy.ui.list.ToDoScreen
import com.beesy.ui.skills.SkillsScreen

const val SCREEN_TRANSITION_MILLIS = 500

@Composable
fun AppNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val startDestination = NavigationBarDestinations.Task
    var selectedDestination by rememberSaveable {
        mutableIntStateOf(startDestination.ordinal) // position in enum
    }

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination.route,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Start,
                tween(
                    SCREEN_TRANSITION_MILLIS,
                ),
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Start,
                tween(
                    SCREEN_TRANSITION_MILLIS,
                ),
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.End,
                tween(
                    SCREEN_TRANSITION_MILLIS,
                ),
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.End,
                tween(
                    SCREEN_TRANSITION_MILLIS,
                ),
            )
        },
    ) {
        NavigationBarDestinations.entries.forEach { destination ->
            composable(destination.route) {
                when (destination) {
                    NavigationBarDestinations.Task -> {
                        ToDoScreen(
                            navigationBar = {
                                Navigation(
                                    selectedDestination = selectedDestination,
                                    navController = navController,
                                    buttonClicked = {
                                        selectedDestination = it
                                    },
                                )
                            },
                        )
                    }

                    NavigationBarDestinations.Skills -> {
                        SkillsScreen(
                            navigationBar = {
                                Navigation(
                                    selectedDestination = selectedDestination,
                                    navController = navController,
                                    buttonClicked = {
                                        selectedDestination = it
                                    },
                                )
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Navigation(
    selectedDestination: Int,
    navController: NavHostController,
    buttonClicked: (Int) -> (Unit),
    modifier: Modifier = Modifier,
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
        modifier = modifier,
        windowInsets = NavigationBarDefaults.windowInsets,
    ) {
        NavigationBarDestinations.entries.forEachIndexed { index, destination ->
            NavigationBarItem(
                colors =
                    NavigationBarItemDefaults.colors(
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                    ),
                selected = selectedDestination == index,
                onClick = {
                    navController.navigate(route = destination.route)
                    buttonClicked(index)
                },
                icon = {
                    Icon(
                        imageVector = destination.icon,
                        contentDescription = stringResource(destination.contentDescriptionId),
                    )
                },
                label = {
                    Text(
                        text = stringResource(destination.labelId),
                    )
                },
            )
        }
    }
}