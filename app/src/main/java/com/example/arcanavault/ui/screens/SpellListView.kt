package com.example.arcanavault.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import com.example.arcanavault.AppState
import com.example.arcanavault.controller.api.ApiClient
import com.example.arcanavault.model.data.IItem
import com.example.arcanavault.model.data.Spell
import com.example.arcanavault.ui.components.Header
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
    var filters by remember { mutableStateOf(emptyMap<String, List<String>>()) }
    var selectedFilters by remember { mutableStateOf(emptyMap<String, List<String>>()) }
    var items by remember { mutableStateOf<List<IItem>>(emptyList()) }
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
                        IconButton(onClick = { showFilterScreen = true }) {
                            Icon(
                                imageVector = Icons.Filled.FilterList,
                                contentDescription = "Open Filter Screen"
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
                SelectedFiltersRow(
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
                            items = fetchFilteredEntities(updatedFilters, apiClient)
                        }
                    }
                )
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
                            items = fetchFilteredEntities(selectedFilters, apiClient)
                        }
                    },
                    onClearAllFilters = {
                        selectedFilters = emptyMap()
                        coroutineScope.launch {
                            items = apiClient.getAllSpells()
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

// Helper function to filter spells by selected filters
suspend fun fetchFilteredEntities(
    selectedFilters: Map<String, List<String>>,
    apiClient: ApiClient
): List<IItem> {
    val allSpells = apiClient.getAllSpells()

    // Basic filter logic based on selectedFilters
    return allSpells.filter { spell ->
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

@Composable
fun SelectedFiltersRow(
    selectedFilters: Map<String, List<String>>,
    onRemoveFilter: (String, String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .horizontalScroll(rememberScrollState())
    ) {
        selectedFilters.forEach { (category, options) ->
            options.forEach { option ->
                FilterTag(
                    category = category,
                    option = option,
                    onRemove = { onRemoveFilter(category, option) }
                )
            }
        }
    }
}

@Composable
fun FilterTag(category: String, option: String, onRemove: () -> Unit) {
    Surface(
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = "$category: $option",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1
            )
            Spacer(modifier = Modifier.width(4.dp))
            IconButton(
                onClick = onRemove,
                modifier = Modifier.size(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Remove Filter",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}
