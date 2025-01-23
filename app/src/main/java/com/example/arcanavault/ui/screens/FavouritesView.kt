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
import androidx.compose.foundation.lazy.rememberLazyListState
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

    // Derived state for favorite spells
    val favoriteSpells by remember(searchQuery, selectedFilters, sortOption, sortOrder) {
        derivedStateOf {
            fetchSpells(searchQuery, selectedFilters, functionsDB)
                .filter { it.isFavorite }
                .sortedWith(getSortComparator(sortOption, sortOrder))
        }
    }

    // Scroll behavior for the top app bar
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = appState.favSavedScrollPosition
    )

    // Save scroll position separately
    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .collect { index ->
                appState.favSavedScrollPosition = index
            }
    }

    // Save sorting and filtering state to appState when they change
    LaunchedEffect(selectedFilters, searchQuery, sortOption, sortOrder) {
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
                scrollBehavior = scrollBehavior
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

            // Main content: Animated filter screen or favorite spells list
            AnimatedVisibility(
                visible = showFilterScreen,
                enter = fadeIn(animationSpec = tween(durationMillis = 200)) +
                        expandVertically(animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy)),
                exit = fadeOut(animationSpec = tween(durationMillis = 200)) +
                        shrinkVertically(animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy))
            ) {
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
            }

            if (!showFilterScreen) {
                if (favoriteSpells.isEmpty()) {
                    Spacer(modifier = Modifier.height(48.dp))
                    Text(
                        text = "No favorites have been saved. Click on the star icon to save a spell to your favorites.",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(8.dp)
                    )
                } else {
                    ListView(
                        listState = listState,
                        items = favoriteSpells,
                        titleProvider = { it.name },
                        detailsProvider = { listOf("Level: ${it.level}", "School: ${it.school.name}") },
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
}
