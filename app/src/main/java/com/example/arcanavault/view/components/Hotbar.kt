package com.example.arcanavault.view.components

import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun Hotbar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier) {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = navController.currentBackStackEntry?.destination?.route == "home",
            onClick = {
                navController.navigate("home") {
                    popUpTo("home") { inclusive = true } // Avoid multiple back stack entries for "home"
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Search, contentDescription = "Search") },
            label = { Text("Search") },
            selected = navController.currentBackStackEntry?.destination?.route == "search",
            onClick = {
                navController.navigate("search") {
                    popUpTo("home") { inclusive = false } // Navigate to "search" and keep "home" in the stack
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Favorite, contentDescription = "Favorite") },
            label = { Text("Favorite") },
            selected = navController.currentBackStackEntry?.destination?.route == "favorite",
            onClick = {
                navController.navigate("favorites") {
                    popUpTo("home") { inclusive = false } // Navigate to "favorite" and keep "home" in the stack
                }
            }
        )
    }
}
