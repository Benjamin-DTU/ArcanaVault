package com.example.arcanavault.ui.screens

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
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
import androidx.navigation.NavController
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
fun SpellListView(
    appState: AppState,
    onSpellSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    functionsDB: FunctionsDB,
) {
    // State variables for UI control
    var showFilterScreen by remember { mutableStateOf(false) }
    var showSearchBar by remember { mutableStateOf(appState.searchQuery.isNotEmpty()) }

    // State variables for filters and sorting
    var filters by remember { mutableStateOf(Spell.generateFilterOptions(appState.listOfSpells)) }
    var selectedFilters by remember { mutableStateOf(appState.selectedFilters) }
    var searchQuery by remember { mutableStateOf(appState.searchQuery) }
    var sortOption by remember { mutableStateOf(appState.sortOption) }
    var sortOrder by remember { mutableStateOf(appState.sortOrderAscending) }

    // List of spells, recomputed only when needed
    val spells by remember(searchQuery, selectedFilters, sortOption, sortOrder) {
        derivedStateOf {
            fetchSpells(searchQuery, selectedFilters, functionsDB)
                .sortedWith(getSortComparator(sortOption, sortOrder))
        }
    }

    // Scroll behavior and state
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = appState.savedScrollPosition
    )

    // Save scroll position separately
    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .collect { index ->
                appState.savedScrollPosition = index
            }
    }

    // Save UI state only when needed
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
                    },
                    {
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
                    AnimatedVisibility(
                        visible = selectedFilters.isNotEmpty(),
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        FilterRow(
                            selectedFilters = selectedFilters,
                            onRemoveFilter = { category, option ->
                                val updatedFilters = selectedFilters.toMutableMap()
                                updatedFilters[category] = updatedFilters[category]?.filterNot { it == option }.orEmpty()
                                if (updatedFilters[category].isNullOrEmpty()) updatedFilters.remove(category)
                                selectedFilters = if (updatedFilters.isEmpty()) {
                                    emptyMap()
                                } else {
                                    updatedFilters
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
            // Search bar with animation
            AnimatedVisibility(
                visible = showSearchBar,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                SearchBar(
                    query = searchQuery,
                    onSearch = { query -> searchQuery = query }
                )
            }

            // Animated FilterView
            AnimatedVisibility(
                visible = showFilterScreen,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
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
                    itemCount = spells.size
                )
            }

            // ListView for spells
            if (!showFilterScreen) {
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

                        val updatedSpells = appState.getListOfSpells().map { s ->
                            if (s.index == spell.index) {
                                s.isFavorite = newFavoriteStatus
                            }
                            s
                        }
                        appState.setListOfSpells(updatedSpells)
                    },
                    modifier = Modifier.fillMaxSize(),
                    listState = listState
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
        (query.isEmpty() || spell.searchCombined.contains(query, ignoreCase = true)) &&
                selectedFilters.all { (category, options) ->
                    options.isEmpty() || when (category) {
                        "Level" -> options.contains(spell.level.toString())
                        "School" -> options.contains(spell.school.name)
                        "Classes" -> spell.classes.any { it.name in options }
                        "Casting Time" -> options.contains(spell.castingTime)
                        "Damage Type" -> options.contains(spell.damage?.damageType?.name ?: "Unknown")
                        "Components" -> options.any { it in spell.components }
                        "Concentration" -> options.contains(spell.concentration.toString())
                        "Ritual" -> options.contains(spell.ritual.toString())
                        else -> true
                    }
                }
    }
}
