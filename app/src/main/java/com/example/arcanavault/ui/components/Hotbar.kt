package com.example.arcanavault.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
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
    scrollToTop: () -> Unit
) {
    // Observe the current back stack entry as state, so recomposition occurs on route changes
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        modifier = modifier.height(65.dp),
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
            icon = { Icon(Icons.Filled.Book, contentDescription = "Home") },
            label = { Text("Spells") },
            selected = currentRoute == Routes.home,
            onClick = {
                if (currentRoute == Routes.home) {
                    navController.navigate(
                        Routes.home,
                        // Pop up to the home route if already on it
                        builder = {
                            popUpTo(Routes.home) { inclusive = true }
                        }
                    )
                    //scrollToTop() // Trigger scroll to top if already on the Home route
                    //TODO this doesn't work. Workaround popUpTo call above is a temporary fix
                } else {
                    navController.navigate(Routes.home) {
                        popUpTo(Routes.home) { inclusive = true }
                    }
                }
            },
            colors = colors,
            modifier = Modifier.size(width = 20.dp, height = 20.dp)
        )

        NavigationBarItem(
            icon = { Icon(Icons.Filled.Favorite, contentDescription = "Favorites") },
            label = { Text("Favorites") },
            selected = currentRoute == Routes.favorites,
            onClick = {
                navController.navigate(Routes.favorites) {
                    popUpTo(Routes.home) { inclusive = false }
                }
            },
            colors = colors,
            modifier = Modifier.size(width = 20.dp, height = 20.dp)
        )
    }
}

