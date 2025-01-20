package com.example.arcanavault.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.example.arcanavault.AppState
import com.example.arcanavault.DB.FunctionsDB
import com.example.arcanavault.model.data.Spell
import com.example.arcanavault.ui.components.FilterRow
import com.example.arcanavault.ui.components.Header
import com.example.arcanavault.ui.components.ListView
import com.example.arcanavault.ui.components.SearchBar
import com.example.arcanavault.ui.components.SortView
import com.example.arcanavault.ui.components.getSortComparator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouritesView(
    appState: AppState,
    onSpellSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    functionsDB: FunctionsDB
) {
    // State variables for UI control
    var showFilterScreen by remember { mutableStateOf(false) }
    var showSearchBar by remember { mutableStateOf(appState.searchQuery.isNotEmpty()) }

    // State variables for filters, sorting, and spells
    var filters by remember { mutableStateOf(Spell.generateFilterOptions(appState.getListOfSpells())) }
    var selectedFilters by remember { mutableStateOf(appState.getSelectedFilters()) }
    var searchQuery by remember { mutableStateOf(appState.getSearchQuery()) }
    var sortOption by remember { mutableStateOf(appState.sortOption) }
    var favoriteSpells by remember { mutableStateOf(emptyList<Spell>()) }

    // Controls the behavior of the top app bar during scrolling
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val scrollFraction = animateFloatAsState(targetValue = (scrollBehavior.state?.collapsedFraction ?: 0f))

    // Fetch spells whenever searchQuery, selectedFilters, or sortOption changes
    LaunchedEffect(searchQuery, selectedFilters, sortOption) {
        favoriteSpells = fetchSpells(searchQuery, selectedFilters, functionsDB)
            .filter { it.isFavorite }
            .sortedWith(getSortComparator(sortOption))
        appState.setSelectedFilters(selectedFilters)
        appState.setSearchQuery(searchQuery)
        appState.sortOption = sortOption
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            // Header with filter, search, and sort buttons
            Header(
                title = "Favorites",
                buttons = listOf(
                    {
                        // Filter button to toggle filter view
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
                    },
                    {
                        // Search button to toggle search bar
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
        ) {
            // Display active filters as tags
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

            // Display search bar if toggled on
            if (showSearchBar) {
                SearchBar(
                    query = searchQuery,
                    onSearch = { query ->
                        searchQuery = query
                    }
                )
            }

            // Show filter view or favorite spells list
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
                Column(modifier = Modifier.fillMaxSize()) {
                    if (favoriteSpells.isEmpty()) {
                        Spacer(modifier = Modifier.height(48.dp))
                        Text(
                            text = "No favorites have been saved. " +
                                    "Click on the star icon to save a spell to your favorites.",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(8.dp)
                        )
                    } else {
                        ListView(
                            items = favoriteSpells,
                            onItemClick = { selectedSpell -> onSpellSelected(selectedSpell) },
                            onFavoriteClick = { spell ->
                                // Remove from favorites and refresh the list
                                functionsDB.removeFromFavorites(spell.index)
                                appState.updateSpellFavoriteStatus(spell.index, false)

                                favoriteSpells = fetchSpells(searchQuery, selectedFilters, functionsDB)
                                    .filter { it.isFavorite }
                                    .sortedWith(getSortComparator(sortOption))

                                val updatedSpells = appState.getListOfSpells().map { s ->
                                    if (s.index == spell.index) {
                                        s.isFavorite = false
                                    }
                                    s
                                }
                                appState.setListOfSpells(updatedSpells)
                            },
                            titleProvider = { spell -> spell.name },
                            detailsProvider = { spell ->
                                listOf(
                                    "Level: ${spell.level}",
                                    "School: ${spell.school.name}"
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}
