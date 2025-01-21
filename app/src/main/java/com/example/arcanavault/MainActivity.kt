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
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.saveable.rememberSaveable


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
                val scrollState = rememberSaveable(saver = ScrollState.Saver) { ScrollState(0) }
                val coroutineScope = rememberCoroutineScope()

                val scrollToTop: () -> Unit = {
                    coroutineScope.launch {
                        if (scrollState.value > 0) {
                            scrollState.animateScrollTo(0)
                        }
                    }
                }

                LaunchedEffect(Unit) {
                    val cachedSpells = functionsDB.getSpellsFromDB()
                    val cachedRules = functionsDB.getRulesFromDB()
                    val cachedConditions = functionsDB.getConditionsFromDB()

                    Log.d("SPELLS_FROM_DB", "Cached spells count: ${cachedSpells.size}")
                    Log.d("RULES_FROM_DB", "Cached rules count: ${cachedRules.size}")
                    Log.d("CONDITIONS_FROM_DB", "Cached conditions count: ${cachedConditions.size}")

                    try {
                        // Fetch data from API
                        val apiSpells = apiClient.getAllSpells()
                        val apiRules = apiClient.getAllRules()
                        val apiConditions = apiClient.getAllConditions()

                        Log.d("API_SPELLS_COUNT", "Fetched ${apiSpells.size} spells from API")
                        Log.d("API_RULES_COUNT", "Fetched ${apiRules.size} rules from API")
                        Log.d("API_CONDITIONS_COUNT", "Fetched ${apiConditions.size} conditions from API")

                        // Sync Spells
                        Log.d("SPELL_SYNC", "Updating spells in DB. Replacing with spells from API.")
                        functionsDB.saveAllSpells(apiSpells)
                        appState.setListOfSpells(apiSpells)
                            //Log.d("SPELL_SYNC", "Spell count matches. Using cached spells from DB.")
                            //appState.setListOfSpells(cachedSpells)


                        // Sync Rules
                        Log.d("RULE_SYNC", "Updating rules in DB. Replacing with rules from API.")
                        functionsDB.saveAllRules(apiRules)
                        appState.setListOfRules(apiRules)
                            //Log.d("RULE_SYNC", "Rule count matches. Using cached rules from DB.")
                            //appState.setListOfRules(cachedRules)


                        // Sync Conditions
                        Log.d("CONDITION_SYNC", "Updating conditions in DB. Replacing with conditions from API.")
                        functionsDB.saveAllConditions(apiConditions)
                        appState.setListOfCondition(apiConditions)
                            //Log.d("CONDITION_SYNC", "Condition count matches. Using cached conditions from DB.")
                            //appState.setListOfCondition(cachedConditions)

                        isLoading = false
                    } catch (e: Exception) {
                        Log.e("SYNC_ERROR", "Error fetching data from API: ${e.message}")

                        // Handle API errors by falling back to cached data
                        if (cachedSpells.isNotEmpty() || cachedRules.isNotEmpty() || cachedConditions.isNotEmpty()) {
                            Log.d("SYNC_ERROR", "API failed. Using cached data from DB.")
                            appState.setListOfSpells(cachedSpells)
                            appState.setListOfRules(cachedRules)
                            appState.setListOfCondition(cachedConditions)
                        } else {
                            Log.e(
                                "SYNC_ERROR",
                                "No cached data available. Showing error UI."
                            )
                        }
                        isLoading = false
                    }
                }


                Scaffold(
                    bottomBar = {
                        Hotbar(
                            navController = navController,
                            scrollToTop = scrollToTop,
                        )
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
                                composable(
                                    Routes.home,
                                    enterTransition = {
                                        when (initialState.destination.route) {
                                            Routes.favorites -> slideIntoContainer(
                                                AnimatedContentTransitionScope.SlideDirection.Right,
                                                tween(700)
                                            )
                                            "details/{selectedSpell}" -> slideIntoContainer(
                                                AnimatedContentTransitionScope.SlideDirection.End,
                                                tween(700)
                                            )
                                            else -> null
                                        }
                                    },
                                    exitTransition = {
                                        when (targetState.destination.route) {
                                            Routes.favorites -> slideOutOfContainer(
                                                AnimatedContentTransitionScope.SlideDirection.Left,
                                                tween(700)
                                            )
                                            "details/{selectedSpell}" -> slideOutOfContainer(
                                                AnimatedContentTransitionScope.SlideDirection.Up,
                                                tween(700)
                                            )
                                            else -> null
                                        }
                                    }
                                ) {
                                    SpellListView(
                                        appState = appState,
                                        scrollState = scrollState,
                                        functionsDB = functionsDB,
                                        onSpellSelected = { selectedSpell ->
                                            navController.navigate("details/${selectedSpell}")
                                        },
                                        modifier = Modifier
                                    )
                                }
                                composable("details/{selectedSpell}",
                                    enterTransition = {
                                        when (initialState.destination.route) {
                                            Routes.home -> slideIntoContainer(
                                                AnimatedContentTransitionScope.SlideDirection.Up,
                                                tween(700)
                                            )
                                            Routes.favorites -> slideIntoContainer(
                                                AnimatedContentTransitionScope.SlideDirection.Up,
                                                tween(700)
                                            )
                                            else -> null
                                        }
                                    },
                                    exitTransition = {
                                        when (targetState.destination.route) {
                                            Routes.home -> slideOutOfContainer(
                                                AnimatedContentTransitionScope.SlideDirection.End,
                                                tween(700)
                                            )
                                            Routes.favorites -> slideOutOfContainer(
                                                AnimatedContentTransitionScope.SlideDirection.Down,
                                                tween(700)
                                            )
                                            else -> null
                                        }
                                    }
                                ){ backStackEntry ->
                                    val spell = appState.getSpellByIndex(backStackEntry.arguments?.getString("selectedSpell"))
                                    SpellDetailsView(appState = appState,spell = spell,functionsDB = functionsDB, onBackClick = { navController.popBackStack() })
                                }
                                composable(Routes.favorites,
                                    enterTransition = {
                                        when (initialState.destination.route) {
                                            Routes.home -> slideIntoContainer(
                                                AnimatedContentTransitionScope.SlideDirection.Left,
                                                tween(700)
                                            )
                                            "details/{selectedSpell}" -> slideIntoContainer(
                                                AnimatedContentTransitionScope.SlideDirection.Down,
                                                tween(700)
                                            )
                                            else -> null
                                        }
                                    },
                                    exitTransition = {
                                        when (targetState.destination.route) {
                                            Routes.home -> slideOutOfContainer(
                                                AnimatedContentTransitionScope.SlideDirection.Right,
                                                tween(700)
                                            )
                                            "details/{selectedSpell}" -> slideOutOfContainer(
                                                AnimatedContentTransitionScope.SlideDirection.Up,
                                                tween(700)
                                            )
                                            else -> null
                                        }
                                    }
                                ) {
                                    FavouritesView(
                                        appState = appState,
                                        functionsDB = functionsDB,
                                        onSpellSelected = { selectedSpell ->
                                            navController.navigate("details/${selectedSpell}")
                                        }
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


