package com.example.arcanavault.ui.screens

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
import com.example.arcanavault.model.data.IItem
import com.example.arcanavault.model.data.Spell
import com.example.arcanavault.ui.components.FilterRow
import com.example.arcanavault.ui.components.Header
import com.example.arcanavault.ui.components.SearchBar
import com.example.arcanavault.ui.components.ListView

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
    var items by remember { mutableStateOf<List<IItem>>(emptyList()) }
    var searchQuery by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    // Fetch spells and filter options once
    LaunchedEffect(Unit) {
        val spells = apiClient.getAllSpells()
        filters = Spell.generateFilterOptions(spells)
        items = spells
        appState.listOfSpells = spells
    }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Header(
                title = "Spells",
                buttons = listOf(
                    {
                        IconButton(onClick = {
                            // Ensure the filter screen closes if the search bar is open
                            showSearchBar = false
                            searchQuery = "" // Reset search query when closing search bar
                            showFilterScreen = true
                        }) {
                            Icon(
                                imageVector = Icons.Filled.FilterList,
                                contentDescription = "Open Filter Screen"
                            )
                        }
                        IconButton(onClick = {
                            // Ensure the search bar closes if the filter screen is open
                            showFilterScreen = false
                            showSearchBar = !showSearchBar
                            if (!showSearchBar) {
                                searchQuery = "" // Reset search query when closing search bar
                                coroutineScope.launch {
                                    items = fetchEntities("", selectedFilters, apiClient)
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
            // Display selected filters as tags
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
                            items = fetchEntities("", updatedFilters, apiClient)
                        }
                    }
                )
            }

            // Render SearchBar
            if (showSearchBar) {
                SearchBar(onSearch = { query ->
                    searchQuery = query
                    coroutineScope.launch {
                        items = fetchEntities(searchQuery, selectedFilters, apiClient)
                    }
                })
            }

            // Conditionally render either the FilterScreen OR the spell list
            if (showFilterScreen) {
                FilterScreen(
                    filterOptions = filters,
                    selectedFilters = selectedFilters,
                    onFilterChange = { category, options ->
                        selectedFilters = selectedFilters.toMutableMap().apply {
                            this[category] = options
                        }
                        coroutineScope.launch {
                            items = fetchEntities(searchQuery, selectedFilters, apiClient)
                        }
                    },
                    onClearAllFilters = {
                        selectedFilters = emptyMap()
                        coroutineScope.launch {
                            items = fetchEntities(searchQuery, selectedFilters, apiClient)
                        }
                    },
                    onNavigateBack = { showFilterScreen = false }
                )
            } else {
                ListView(
                    items = items,
                    appState = appState,
                    onItemClick = { selectedSpell -> onSpellSelected(selectedSpell) }
                )
            }
        }
    }
}


// Helper function to filter spells by selected filters and search query
suspend fun fetchEntities(
    query: String,
    selectedFilters: Map<String, List<String>>,
    apiClient: ApiClient
): List<IItem> {
    val allSpells = apiClient.getAllSpells()

    return allSpells.filter { spell ->
        // Check if the spell matches the search query
        (query.isEmpty() || spell.name.startsWith(query, ignoreCase = true)) &&
                // Check if the spell matches the selected filters
                selectedFilters.all { (category, options) ->
                    when (category) {
                        "Level"         -> options.contains(spell.level.toString())
                        "School"        -> options.contains(spell.school?.name.toString())
                        "Classes"       -> spell.classes.any { it.name in options }
                        "Casting Time"  -> options.contains(spell.castingTime)
                        "Damage Type"   -> options.contains(spell.damage?.damageType?.name.toString())
                        "Components"    -> options.any { it in spell.components }
                        "Concentration" -> options.contains(spell.concentration.toString())
                        "Ritual"        -> options.contains(spell.ritual.toString())
                        else            -> true
                    }
                }
    }
}


