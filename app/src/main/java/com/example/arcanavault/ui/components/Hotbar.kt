package com.example.arcanavault.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.arcanavault.Routes

@Composable
fun Hotbar(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    NavigationBar(modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface //TODO figure out why this is not showing the correct color
                ,tonalElevation = 0.dp
    )  {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = navController.currentBackStackEntry?.destination?.route == Routes.home,
            onClick = {
                navController.navigate(Routes.home) {
                    popUpTo(Routes.home) { inclusive = true } // Avoid multiple back stack entries for "home"
                }
            }

        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Favorite, contentDescription = "Favorite") },
            label = { Text("Favorite") },
            selected = navController.currentBackStackEntry?.destination?.route == Routes.favorites,
            onClick = {
                navController.navigate(Routes.favorites) {
                    popUpTo(Routes.home) { inclusive = false } // Navigate to "favorite" and keep "home" in the stack
                }
            }
        )
    }
}
