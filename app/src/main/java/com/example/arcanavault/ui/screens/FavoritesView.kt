package com.example.arcanavault.ui.screens

import FilterRow
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.example.arcanavault.AppState
import com.example.arcanavault.DB.FunctionsDB
import com.example.arcanavault.model.data.Spell
import com.example.arcanavault.ui.components.Header
import com.example.arcanavault.ui.components.ListView
import com.example.arcanavault.ui.components.SearchBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouritesView(
    appState: AppState,
    onSpellSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showFilterScreen by remember { mutableStateOf(false) }
    var showSearchBar by remember { mutableStateOf(false) }
    var filters by remember { mutableStateOf(Spell.generateFilterOptions(appState.getListOfSpells())) }
    var selectedFilters by remember { mutableStateOf(emptyMap<String, List<String>>()) }
    var searchQuery by remember { mutableStateOf("") }

    val functionsDB = remember { FunctionsDB() }
    var favoriteSpells by remember { mutableStateOf(listOf<Spell>()) }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val scrollFraction = animateFloatAsState(targetValue = (scrollBehavior.state?.collapsedFraction ?: 0f))

    LaunchedEffect(Unit) {
        favoriteSpells = withContext(Dispatchers.IO) {
            functionsDB.getFavoriteSpells()
        }
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Header(
                title = "Favorites",
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
                Column(modifier = modifier.fillMaxSize()) {
                    if (favoriteSpells.isEmpty()) {
                        Spacer(modifier = Modifier.height(48.dp))
                        Text(
                            text = "No favourites have been saved. " +
                                    "Click on the star icon to save a spell to your favourites.",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(8.dp)
                        )
                    } else {
                        ListView(
                            items = favoriteSpells.filter { spell ->
                                (searchQuery.isEmpty() || spell.name.contains(searchQuery, ignoreCase = true)) &&
                                        selectedFilters.all { (category, options) ->
                                            when (category) {
                                                "Level" -> options.contains(spell.level.toString())
                                                "School" -> options.contains(spell.school.name)
                                                else -> true
                                            }
                                        }
                            },
                            onItemClick = { selectedSpell -> onSpellSelected(selectedSpell) },
                            onFavoriteClick = { spell ->

                                functionsDB.removeFromFavorites(spell.index)

                                appState.updateSpellFavoriteStatus(spell.index, false)

                                favoriteSpells = favoriteSpells.filter { it.index != spell.index }

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
