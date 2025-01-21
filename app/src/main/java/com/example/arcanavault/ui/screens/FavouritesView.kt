package com.example.arcanavault.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import com.example.arcanavault.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouritesView(
    appState: AppState,
    onSpellSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    functionsDB: FunctionsDB
) {
    // UI state variables
    var showFilterScreen by remember { mutableStateOf(false) }
    var showSearchBar by remember { mutableStateOf(appState.searchQuery.isNotEmpty()) }

    // Sorting and filtering states
    var filters by remember { mutableStateOf(Spell.generateFilterOptions(appState.listOfSpells)) }
    var selectedFilters by remember { mutableStateOf(appState.selectedFilters) }
    var searchQuery by remember { mutableStateOf(appState.searchQuery) }
    var sortOption by remember { mutableStateOf(appState.sortOption) }
    var sortOrder by remember { mutableStateOf(appState.sortOrderAscending) }
    var favoriteSpells by remember { mutableStateOf(emptyList<Spell>()) }

    // Scroll behavior for the top app bar
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    // Fetch favorite spells when search, filter, or sort options change
    LaunchedEffect(searchQuery, selectedFilters, sortOption, sortOrder) {
        favoriteSpells = fetchSpells(searchQuery, selectedFilters, functionsDB)
            .filter { it.isFavorite }
            .sortedWith(getSortComparator(sortOption, sortOrder))
        appState.selectedFilters = selectedFilters
        appState.searchQuery = searchQuery
        appState.sortOption = sortOption
        appState.sortOrderAscending = sortOrder
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Header(
                title = "Favorites",
                buttons = listOf(
                    {
                        // Filter button
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
                        // Search button
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
                        // Sort button
                        SortView(
                            selectedSort = sortOption,
                            isSortOrderAscending = sortOrder,
                            onSortSelected = { selected, ascending ->
                                sortOption = selected
                                sortOrder = ascending
                            }
                        )
                    }
                ),
                scrollBehavior = scrollBehavior,
                content = {
                    if (selectedFilters.isNotEmpty()) {
                        FilterRow(
                            selectedFilters = selectedFilters,
                            onRemoveFilter = { category, option ->
                                val updatedFilters = selectedFilters.toMutableMap()
                                updatedFilters[category] = updatedFilters[category]?.filterNot { it == option }.orEmpty()
                                if (updatedFilters[category].isNullOrEmpty()) updatedFilters.remove(category)
                                if (updatedFilters.isEmpty()) {
                                    // Reset filters when no filters are left
                                    selectedFilters = emptyMap()
                                } else {
                                    selectedFilters = updatedFilters
                                }
                            },
                            scrollFraction = scrollBehavior.state.collapsedFraction ?: 0f,
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Search bar
            AnimatedVisibility(
                visible = showSearchBar,
                enter = fadeIn(animationSpec = tween(durationMillis = 200)) +
                        expandVertically(animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy)),
                exit = fadeOut(animationSpec = tween(durationMillis = 200)) +
                        shrinkVertically(animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy))
            ) {
                SearchBar(
                    query = searchQuery,
                    onSearch = { query -> searchQuery = query }
                )
            }

            // Main content: filter view or favorite spells list
            if (showFilterScreen) {
                FilterView(
                    filterOptions = filters,
                    selectedFilters = selectedFilters,
                    onFilterChange = { category, options ->
                        val updatedFilters = selectedFilters.toMutableMap().apply {
                            if (options.isEmpty()) {
                                remove(category)
                            } else {
                                this[category] = options
                            }
                        }
                        selectedFilters = if (updatedFilters.isEmpty()) {
                            emptyMap() // Reset filters when no filters are left
                        } else {
                            updatedFilters
                        }
                    },
                    onClearAllFilters = { selectedFilters = emptyMap() },
                    itemCount = favoriteSpells.size
                )
            } else {
                if (favoriteSpells.isEmpty()) {
                    Spacer(modifier = Modifier.height(48.dp))
                    Text(
                        text = "No favorites have been saved. Click on the star icon to save a spell to your favorites.",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(8.dp)
                    )
                } else {
                    ListView(
                        items = favoriteSpells,
                        titleProvider = { it.name },
                        detailsProvider = { listOf("Level: ${it.level}", "School: ${it.school.name}") },
                        onItemClick = onSpellSelected,
                        onFavoriteClick = { spell ->
                            functionsDB.removeFromFavorites(spell.index)
                            appState.updateSpellFavoriteStatus(spell.index, false)

                            // Refresh favorite spells
                            favoriteSpells = fetchSpells(searchQuery, selectedFilters, functionsDB)
                                .filter { it.isFavorite }
                                .sortedWith(getSortComparator(sortOption, sortOrder))

                            val updatedSpells = appState.listOfSpells.map { s ->
                                if (s.index == spell.index) {
                                    s.isFavorite = false
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
}
