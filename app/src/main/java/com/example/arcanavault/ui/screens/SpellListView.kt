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
import kotlinx.coroutines.launch
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
    apiClient: ApiClient,
    appState: AppState,
    onSpellSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showFilterScreen by remember { mutableStateOf(false) }
    var showSearchBar by remember { mutableStateOf(false) }
    var filters by remember { mutableStateOf(emptyMap<String, List<String>>()) }
    var selectedFilters by remember { mutableStateOf(emptyMap<String, List<String>>()) }
    var spells by remember { mutableStateOf<List<Spell>>(emptyList()) }
    var searchQuery by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    val functionsDB = remember { FunctionsDB() }

    LaunchedEffect(Unit) {
        val fetchedSpells = apiClient.getAllSpells()
        filters = Spell.generateFilterOptions(fetchedSpells)
        spells = fetchedSpells
        appState.listOfSpells = fetchedSpells
    }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val scrollFraction = animateFloatAsState(
        targetValue = (scrollBehavior.state?.collapsedFraction ?: 0f)
    )

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
                            showFilterScreen = !showFilterScreen // Toggle the filter screen
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
                                coroutineScope.launch {
                                    spells = fetchSpells("", selectedFilters, apiClient)
                                }
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
                        val updatedOptions = updatedFilters[category]?.toMutableList()
                        updatedOptions?.remove(option)
                        if (updatedOptions.isNullOrEmpty()) {
                            updatedFilters.remove(category)
                        } else {
                            updatedFilters[category] = updatedOptions
                        }
                        selectedFilters = updatedFilters
                        coroutineScope.launch {
                            spells = fetchSpells(searchQuery, updatedFilters, apiClient)
                        }
                    },
                    scrollFraction = scrollFraction.value
                )
            }

            if (showSearchBar) {
                SearchBar(onSearch = { query ->
                    searchQuery = query
                    coroutineScope.launch {
                        spells = fetchSpells(searchQuery, selectedFilters, apiClient)
                    }
                })
            }

            if (showFilterScreen) {
                FilterView(
                    filterOptions = filters,
                    selectedFilters = selectedFilters,
                    onFilterChange = { category, options ->
                        selectedFilters = selectedFilters.toMutableMap().apply {
                            this[category] = options
                        }
                        coroutineScope.launch {
                            spells = fetchSpells(searchQuery, selectedFilters, apiClient)
                        }
                    },
                    onClearAllFilters = {
                        selectedFilters = emptyMap()
                        coroutineScope.launch {
                            spells = fetchSpells(searchQuery, selectedFilters, apiClient)
                        }
                    },
                )
            } else {
                ListView(
                    items = spells,
                    titleProvider = { spell -> spell.name },
                    detailsProvider = { spell ->
                        listOf(
                            "Level: ${spell.level}",
                            "School: ${spell.school.name}"
                        )
                    },
                    onItemClick = { selectedSpell -> onSpellSelected(selectedSpell) },
                    onFavoriteClick = { spell ->
                        functionsDB.addToFavorites(spell)
                    }
                )
            }
        }
    }
}

// Helper function to filter spells by selected filters and search query
suspend fun fetchSpells(
    query: String,
    selectedFilters: Map<String, List<String>>,
    apiClient: ApiClient
): List<Spell> {
    val allSpells = apiClient.getAllSpells()

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
