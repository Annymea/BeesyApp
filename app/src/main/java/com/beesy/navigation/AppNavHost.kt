package com.beesy.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.beesy.ui.list.ToDoScreen

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
                        Scaffold(
                            bottomBar = {
                                Navigation(
                                    selectedDestination = selectedDestination,
                                    navController = navController,
                                    buttonClicked = {
                                        selectedDestination = it
                                    },
                                )
                            },
                        ) { innerPadding ->
                            Text(
                                modifier = Modifier.padding(innerPadding),
                                text = "Hier könnten ihre Skills stehen",
                            )
                        }
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
        modifier = modifier,
        windowInsets = NavigationBarDefaults.windowInsets,
    ) {
        NavigationBarDestinations.entries.forEachIndexed { index, destination ->
            NavigationBarItem(
                selected = selectedDestination == index,
                onClick = {
                    navController.navigate(route = destination.route)
                    buttonClicked(index)
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "test",
                    )
                },
                label = { Text("test") },
            )
        }
    }
}