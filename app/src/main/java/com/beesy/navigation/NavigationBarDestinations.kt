package com.beesy.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector
import com.beesy.R

enum class NavigationBarDestinations(
    val route: String,
    val labelId: Int,
    val icon: ImageVector,
    val contentDescriptionId: Int,
) {
    Task(
        route = "tasks",
        labelId = R.string.tasks,
        icon = Icons.Default.Menu,
        contentDescriptionId = R.string.task_list,
    ),
    Skills(
        route = "skills",
        labelId = R.string.skills,
        icon = Icons.Default.Star,
        contentDescriptionId = R.string.skills,
    ),
}