package com.example.arcanavault.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.example.arcanavault.AppState
import com.example.arcanavault.model.data.Spell
import com.example.arcanavault.ui.components.Header
import com.example.arcanavault.DB.FunctionsDB
import com.example.arcanavault.ui.components.SearchBar
import com.example.arcanavault.ui.components.ListView
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.verticalScroll
import com.example.arcanavault.ui.components.FilterRow
import com.example.arcanavault.ui.components.SortView
import com.example.arcanavault.ui.components.getSortComparator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpellListView(
    appState: AppState,
    onSpellSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    functionsDB: FunctionsDB,
    scrollState: ScrollState
) {
    // State variables for UI control
    var showFilterScreen by remember { mutableStateOf(false) } // Toggles filter view
    var showSearchBar by remember { mutableStateOf(appState.searchQuery.isNotEmpty()) } // Toggles search bar visibility

    // State variables for filters and sorting
    var filters by remember { mutableStateOf(Spell.generateFilterOptions(appState.listOfSpells)) } // Filter options based on spells
    var selectedFilters by remember { mutableStateOf(appState.selectedFilters) } // Current selected filters
    var searchQuery by remember { mutableStateOf(appState.searchQuery) } // Current search query
    var sortOption by remember { mutableStateOf(appState.sortOption) } // Get initial sort option from AppState

    // Holds the currently displayed spells
    var spells by remember { mutableStateOf(emptyList<Spell>()) }

    // Controls the behavior of the top app bar during scrolling
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val scrollFraction = animateFloatAsState(targetValue = (scrollBehavior.state.collapsedFraction
        ?: 0f))

    // Recomputes the spells list whenever searchQuery, selectedFilters, or sortOption changes
    LaunchedEffect(searchQuery, selectedFilters, sortOption) {
        spells = fetchSpells(searchQuery, selectedFilters, functionsDB).sortedWith(getSortComparator(sortOption))
        appState.selectedFilters = selectedFilters // Save selected filters to AppState
        appState.searchQuery = searchQuery         // Save search query to AppState
        appState.sortOption = sortOption          // Save sort option to AppState
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            // Header with filter, search, and sort buttons
            Header(
                title = "Spells",
                buttons = listOf(
                    {
                        // Filter button to toggle filter view
                        IconButton(onClick = {
                            showSearchBar = false // Close search bar when filter view is opened
                            searchQuery = "" // Clear search query when toggling filters
                            showFilterScreen = !showFilterScreen // Toggle filter view
                        }) {
                            Icon(
                                imageVector = Icons.Filled.FilterList,
                                contentDescription = if (showFilterScreen) "Close Filter Screen" else "Open Filter Screen"
                            )
                        }
                    },
                    {
                        // Search button to toggle search bar
                        IconButton(onClick = {
                            showFilterScreen = false // Close filter view when search bar is opened
                            showSearchBar = !showSearchBar // Toggle search bar visibility
                            if (!showSearchBar) {
                                searchQuery = "" // Clear search query when search bar is closed
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = if (showSearchBar) "Close Search Field" else "Open Search Field"
                            )
                        }
                    },
                    {
                        // Sort button to display sorting options
                        SortView(
                            onSortSelected = { selectedSort ->
                                sortOption = selectedSort
                            }
                        )
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
                .verticalScroll(scrollState)
        ) {
            // Display active filters as tags
            if (selectedFilters.isNotEmpty()) {
                FilterRow(
                    selectedFilters = selectedFilters,
                    onRemoveFilter = { category, option ->
                        // Remove selected filter
                        val updatedFilters = selectedFilters.toMutableMap()
                        updatedFilters[category] = updatedFilters[category]?.filterNot { it == option }.orEmpty()
                        if (updatedFilters[category].isNullOrEmpty()) updatedFilters.remove(category)
                        selectedFilters = updatedFilters
                    },
                    scrollFraction = scrollFraction.value
                )
            }

            // Display search bar if toggled on
            if (showSearchBar) {
                SearchBar(
                    query = searchQuery, // Pass current search query
                    onSearch = { query ->
                        searchQuery = query // Update search query as user types
                    }
                )
            }

            // Show filter view or spell list
            if (showFilterScreen) {
                FilterView(
                    filterOptions = filters,
                    selectedFilters = selectedFilters,
                    onFilterChange = { category, options ->
                        // Update selected filters
                        selectedFilters = selectedFilters.toMutableMap().apply { this[category] = options }
                    },
                    onClearAllFilters = { selectedFilters = emptyMap() } // Clear all filters
                )
            } else {
                // ListView displaying filtered and sorted spells
                ListView(
                    items = spells,
                    titleProvider = { spell -> spell.name },
                    detailsProvider = { spell ->
                        listOf("Level: ${spell.level}", "School: ${spell.school.name}")
                    },
                    onItemClick = onSpellSelected,
                    onFavoriteClick = { spell ->

                        val newFavoriteStatus = !spell.isFavorite
                        spell.isFavorite = newFavoriteStatus

                        if (newFavoriteStatus) {
                            functionsDB.addToFavorites(spell)
                        } else {
                            functionsDB.removeFromFavorites(spell.index)
                        }


                        appState.updateSpellFavoriteStatus(spell.index, newFavoriteStatus)

                        // Refresh spell list in AppState
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
        (query.isEmpty() || spell.searchCombined.contains(query, ignoreCase = true)) &&
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
