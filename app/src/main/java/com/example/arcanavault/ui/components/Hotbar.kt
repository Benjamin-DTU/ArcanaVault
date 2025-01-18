package com.example.arcanavault.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.arcanavault.Routes

@Composable
fun Hotbar(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    // Observe the current back stack entry as state, so recomposition occurs on route changes
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp,
    ) {
        val colors = NavigationBarItemColors(
            selectedIconColor = MaterialTheme.colorScheme.primary,
            selectedTextColor = MaterialTheme.colorScheme.primary,
            selectedIndicatorColor = Color.Transparent,
            unselectedIconColor = MaterialTheme.colorScheme.onSurface,
            unselectedTextColor = MaterialTheme.colorScheme.onSurface,
            disabledIconColor = MaterialTheme.colorScheme.onSurface,
            disabledTextColor = MaterialTheme.colorScheme.onSurface
        )

        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = currentRoute == Routes.home,
            onClick = {
                navController.navigate(Routes.home) {
                    popUpTo(Routes.home) { inclusive = true }
                }
            },
            colors = colors
        )

        NavigationBarItem(
            icon = { Icon(Icons.Filled.Favorite, contentDescription = "Favorite") },
            label = { Text("Favorite") },
            selected = currentRoute == Routes.favorites,
            onClick = {
                navController.navigate(Routes.favorites) {
                    popUpTo(Routes.home) { inclusive = false }
                }
            },
            colors = colors
        )
    }
}

