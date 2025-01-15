package com.example.arcanavault.ui.components

import android.content.res.Resources
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.arcanavault.Routes

@Composable
fun Hotbar(
    navController: NavController,
    modifier: Modifier = Modifier,
    hotBarColor: Color = MaterialTheme.colorScheme.surface
) {
    NavigationBar(modifier = modifier,
        containerColor = hotBarColor //TODO figure out why this is not showing the correct color
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
