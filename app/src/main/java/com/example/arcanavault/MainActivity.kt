package com.example.arcanavault

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.arcanavault.controller.api.ApiClient
import com.example.arcanavault.view.components.FetchingDataView
import com.example.arcanavault.view.components.Hotbar
import com.example.arcanavault.view.screens.FavouritesView
import com.example.arcanavault.view.screens.SearchView
import com.example.arcanavault.view.screens.SpellListView
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val apiClient = ApiClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                var isLoading by remember { mutableStateOf(true) }
                val selectedScreen = remember { mutableStateOf("home") }
                val coroutineScope = rememberCoroutineScope()
                val navController = rememberNavController()

                LaunchedEffect(Unit) {
                    coroutineScope.launch {
                        // Fetch spells
                        apiClient.getAllSpells()
                        isLoading = false
                    }
                }

                Scaffold(
                    bottomBar = {
                        Hotbar(navController = navController)
                    }
                ) { paddingValues ->
                    Surface(modifier = Modifier.padding(paddingValues)) {
                        if (isLoading) {
                            FetchingDataView()
                        } else {
                            // Navigation logic is managed entirely by NavHost
                            NavHost(
                                navController = navController,
                                startDestination = "home"
                            ) {
                                composable("home") {
                                    SpellListView(apiClient = apiClient, navController = navController)
                                }
                                composable("search") {
                                    SearchView(apiClient = apiClient, navController = navController)
                                }
                                composable("favorites") {
                                    FavouritesView(apiClient = apiClient, navController = navController)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
