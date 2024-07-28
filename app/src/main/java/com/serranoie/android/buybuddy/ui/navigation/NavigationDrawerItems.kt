package com.serranoie.android.buybuddy.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Backup
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material.icons.rounded.Backup
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.ThumbUp
import androidx.compose.ui.graphics.vector.ImageVector

data class NavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: String,
)

val items = listOf(
    NavigationItem(
        title = "Home",
        selectedIcon = Icons.Rounded.Home,
        unselectedIcon = Icons.Outlined.Home,
        route = Screen.HOME.name
    ),
    NavigationItem(
        title = "Summary",
        selectedIcon = Icons.Rounded.BarChart,
        unselectedIcon = Icons.Outlined.BarChart,
        route = Screen.SUMMARY.name
    ),
    NavigationItem(
        title = "Backup",
        selectedIcon = Icons.Rounded.Backup,
        unselectedIcon = Icons.Outlined.Backup,
        route = Screen.BACKUP.name
    ),
    NavigationItem(
        title = "Settings",
        selectedIcon = Icons.Rounded.Settings,
        unselectedIcon = Icons.Outlined.Settings,
        route = Screen.SETTINGS.name
    ),
    NavigationItem(
        title = "Rate Us",
        selectedIcon = Icons.Rounded.ThumbUp,
        unselectedIcon = Icons.Outlined.ThumbUp,
        route = Screen.HOME.name
    ),
    NavigationItem(
        title = "Share",
        selectedIcon = Icons.Rounded.Share,
        unselectedIcon = Icons.Outlined.Share,
        route = Screen.HOME.name
    ),
)