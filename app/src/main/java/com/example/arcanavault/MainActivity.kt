package com.example.arcanavault

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
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
import com.example.arcanavault.ui.components.FetchingDataView
import com.example.arcanavault.ui.components.Hotbar
import com.example.arcanavault.ui.screens.FavouritesView
import com.example.arcanavault.ui.screens.SearchView
import com.example.arcanavault.ui.screens.SpellDetailsView
import com.example.arcanavault.ui.screens.SpellListView
import kotlinx.coroutines.launch
import com.example.arcanavault.ui.theme.ArcanaVaultTheme


class MainActivity : ComponentActivity() {
    private val apiClient = ApiClient()
    private val appState = AppState();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
             val darkTheme = isSystemInDarkTheme()

                ArcanaVaultTheme(darkTheme = darkTheme) {
                var isLoading by remember { mutableStateOf(true) }
                val coroutineScope = rememberCoroutineScope()
                val navController = rememberNavController()

                LaunchedEffect(Unit) {
                    coroutineScope.launch {
                        apiClient.getAllSpells()
                        isLoading = false
                    }
                }

                Scaffold(
                    bottomBar = {
                        Hotbar(navController = navController)
                    }
                ) { paddingValues ->
                    Surface(
                        modifier = Modifier.padding(paddingValues),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        if (isLoading) {
                            FetchingDataView()
                        } else {
                            NavHost(
                                navController = navController,
                                startDestination = Routes.home
                            ) {
                                composable(Routes.home) {
                                    SpellListView(
                                        apiClient = apiClient,
                                        appState = appState,
                                        onSpellSelected = { selectedSpell ->
                                            navController.navigate("details/${selectedSpell}")
                                        }
                                    )
                                }
                                composable("details/{selectedSpell}") { it ->
                                    val spell = appState.getSpellByIndex(it.arguments?.getString("selectedSpell"))
                                    SpellDetailsView(
                                        spell = spell,
                                        onBackClick = { navController.popBackStack() }
                                    )
                                }
                                composable(Routes.search) {
                                    SearchView(apiClient = apiClient, navController = navController)
                                }
                                composable(Routes.favorites) {
                                    FavouritesView(
                                        appState = appState,
                                        onSpellSelected = { selectedSpell ->
                                            navController.navigate("details/${selectedSpell}")
                                        },
                                        onBackClick = { navController.popBackStack() }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

