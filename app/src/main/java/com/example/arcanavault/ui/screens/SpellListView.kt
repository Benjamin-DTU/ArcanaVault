package com.example.arcanavault.ui.screens

import FilterRow
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.example.arcanavault.AppState
import com.example.arcanavault.controller.api.ApiClient
import com.example.arcanavault.model.data.Spell
import com.example.arcanavault.ui.components.Header
import com.example.arcanavault.DB.FunctionsDB
import com.example.arcanavault.ui.components.SearchBar
import com.example.arcanavault.ui.components.ListView
import androidx.compose.animation.core.animateFloatAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpellListView(
    appState: AppState,
    onSpellSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    functionsDB: FunctionsDB
) {
    var showFilterScreen by remember { mutableStateOf(false) }
    var showSearchBar by remember { mutableStateOf(false) }
    var filters by remember { mutableStateOf(Spell.generateFilterOptions(appState.getListOfSpells())) }
    var selectedFilters by remember { mutableStateOf(emptyMap<String, List<String>>()) }
    var searchQuery by remember { mutableStateOf("") }

    var spells by remember { mutableStateOf(emptyList<Spell>()) }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val scrollFraction = animateFloatAsState(targetValue = (scrollBehavior.state?.collapsedFraction ?: 0f))

    // Fetch spells whenever searchQuery or selectedFilters change
    LaunchedEffect(searchQuery, selectedFilters) {
        spells = fetchSpells(searchQuery, selectedFilters, functionsDB)
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Header(
                title = "Spells",
                buttons = listOf(
                    {
                        IconButton(onClick = {
                            showSearchBar = false
                            searchQuery = ""
                            showFilterScreen = !showFilterScreen
                        }) {
                            Icon(
                                imageVector = Icons.Filled.FilterList,
                                contentDescription = if (showFilterScreen) "Close Filter Screen" else "Open Filter Screen"
                            )
                        }
                        IconButton(onClick = {
                            showFilterScreen = false
                            showSearchBar = !showSearchBar
                            if (!showSearchBar) {
                                searchQuery = ""
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = if (showSearchBar) "Close Search Field" else "Open Search Field"
                            )
                        }
                    }
                ),
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (selectedFilters.isNotEmpty()) {
                FilterRow(
                    selectedFilters = selectedFilters,
                    onRemoveFilter = { category, option ->
                        val updatedFilters = selectedFilters.toMutableMap()
                        updatedFilters[category] = updatedFilters[category]?.filterNot { it == option }.orEmpty()
                        if (updatedFilters[category].isNullOrEmpty()) updatedFilters.remove(category)
                        selectedFilters = updatedFilters
                    },
                    scrollFraction = scrollFraction.value
                )
            }

            if (showSearchBar) {
                SearchBar(onSearch = { query ->
                    searchQuery = query
                })
            }

            if (showFilterScreen) {
                FilterView(
                    filterOptions = filters,
                    selectedFilters = selectedFilters,
                    onFilterChange = { category, options ->
                        selectedFilters = selectedFilters.toMutableMap().apply { this[category] = options }
                    },
                    onClearAllFilters = { selectedFilters = emptyMap() }
                )
            } else {
                ListView(
                    items = spells,
                    titleProvider = { spell -> spell.name },
                    detailsProvider = { spell ->
                        listOf("Level: ${spell.level}", "School: ${spell.school.name}")
                    },
                    onItemClick = onSpellSelected,
                    onFavoriteClick = { spell ->
                        val newFavoriteStatus = !spell.isFavorite
                        spell.isFavorite = !spell.isFavorite
                        if (newFavoriteStatus) {
                            functionsDB.addToFavorites(spell)
                        } else {
                            functionsDB.removeFromFavorites(spell.index)
                        }

                        appState.updateSpellFavoriteStatus(spell.index, newFavoriteStatus)

                        val updatedSpells = appState.getListOfSpells().map { s ->
                            if (s.index == spell.index) {
                                s.isFavorite = newFavoriteStatus
                            }
                            s
                        }
                        appState.setListOfSpells(updatedSpells)
                    }
                )
            }
        }
    }
}


// Helper function to filter spells by selected filters and search query
fun fetchSpells(
    query: String,
    selectedFilters: Map<String, List<String>>,
    functionsDB: FunctionsDB
): List<Spell> {
    val allSpells = functionsDB.getSpellsFromDB()

    return allSpells.filter { spell ->
        // Check if the spell matches the search query
        (query.isEmpty() || spell.name.startsWith(query, ignoreCase = true)) &&
                // Check if the spell matches the selected filters
                selectedFilters.all { (category, options) ->
                    when (category) {
                        "Level"         -> options.contains(spell.level.toString())
                        "School"        -> options.contains(spell.school.name)
                        "Classes"       -> spell.classes.any { it.name in options }
                        "Casting Time"  -> options.contains(spell.castingTime)
                        "Damage Type"   -> options.contains(spell.damage?.damageType?.name ?: "Unknown")
                        "Components"    -> options.any { it in spell.components }
                        "Concentration" -> options.contains(spell.concentration.toString())
                        "Ritual"        -> options.contains(spell.ritual.toString())
                        else            -> true
                    }
                }
    }
}

