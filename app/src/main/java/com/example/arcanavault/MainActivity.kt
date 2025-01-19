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
import com.example.arcanavault.ui.screens.SpellDetailsView
import com.example.arcanavault.ui.screens.SpellListView
import kotlinx.coroutines.launch
import com.example.arcanavault.ui.theme.ArcanaVaultTheme
import com.example.arcanavault.DB.FunctionsDB
import android.util.Log



class MainActivity : ComponentActivity() {
    private val apiClient = ApiClient()
    private val appState = AppState()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val darkTheme = isSystemInDarkTheme()
            ArcanaVaultTheme(darkTheme = darkTheme) {
                var isLoading by remember { mutableStateOf(true) }
                val navController = rememberNavController()
                val functionsDB = remember { FunctionsDB() }

                LaunchedEffect(Unit) {
                    val cachedSpells = functionsDB.getSpellsFromDB()
                    Log.d("SPELLS_FROM_DB", "Cached spells count: ${cachedSpells.size}")

                    try {
                        val apiRules = apiClient.getAllRules()
                        val apiConditions = apiClient.getAllConditions()
                        val apiSpells = apiClient.getAllSpells()
                        Log.d("API_SPELLS_COUNT", "Fetched ${apiSpells.size} spells from API")

                        if (cachedSpells.size != apiSpells.size) {

                            Log.d("SPELL_SYNC", "Updating spells in DB. Replacing with spells from API.")
                            functionsDB.saveAllSpells(apiSpells)
                            appState.setListOfSpells(apiSpells)
                            appState.setListOfRules(apiRules)
                            appState.setListOfCondition(apiConditions)
                        } else {

                            Log.d("SPELL_SYNC", "Spell count matches. Using cached spells from DB.")
                            appState.setListOfSpells(cachedSpells)
                        }
                        isLoading = false
                    } catch (e: Exception) {
                        Log.e("SPELL_SYNC_ERROR", "Error fetching spells from API: ${e.message}")

                        if (cachedSpells.isNotEmpty()) {
                            Log.d("SPELL_SYNC", "API failed. Using cached spells from DB.")
                            appState.setListOfSpells(cachedSpells)
                        } else {
                            Log.e(
                                "SPELL_SYNC_ERROR",
                                "No cached spells available. Showing error UI."
                            )
                        }
                        isLoading = false
                    }}

                Scaffold(
                    bottomBar = { Hotbar(navController = navController) }
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
                                        appState = appState,
                                        functionsDB = functionsDB,
                                        onSpellSelected = { selectedSpell ->
                                            navController.navigate("details/${selectedSpell}")
                                        }
                                    )
                                }
                                composable("details/{selectedSpell}") { backStackEntry ->
                                    val spell = appState.getSpellByIndex(backStackEntry.arguments?.getString("selectedSpell"))
                                    SpellDetailsView(appState = appState,spell = spell, onBackClick = { navController.popBackStack() })
                                }
                                composable(Routes.favorites) {
                                    FavouritesView(
                                        appState = appState,
                                        functionsDB = functionsDB,
                                        onSpellSelected = { selectedSpell -> navController.navigate("details/${selectedSpell}") }
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


