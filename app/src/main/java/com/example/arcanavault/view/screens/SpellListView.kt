package com.example.arcanavault.view.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.arcanavault.controller.api.ApiClient
import com.example.arcanavault.model.data.IItem
import com.example.arcanavault.model.data.Spell
import com.example.arcanavault.view.components.FilterView
import com.example.arcanavault.view.components.ListView
import kotlinx.coroutines.launch

@Composable
fun SpellListView(
    apiClient: ApiClient,
    modifier: Modifier = Modifier
) {
    var filters by remember { mutableStateOf(emptyMap<String, List<String>>()) }
    var selectedFilters by remember { mutableStateOf(emptyMap<String, List<String>>()) }
    var entities by remember { mutableStateOf<List<IItem>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()

    // Fetch spells from API and generate filter options
    LaunchedEffect(Unit) {
        val spells = apiClient.getAllSpells()
        filters = Spell.generateFilterOptions(spells)
        entities = spells
    }

    Column(modifier = modifier.fillMaxSize()) {
        FilterView(
            filterOptions = filters,
            selectedFilters = selectedFilters,
            onFilterChange = { category, options ->
                selectedFilters = selectedFilters.toMutableMap().apply { this[category] = options }
                coroutineScope.launch {
                    entities = fetchFilteredEntities(selectedFilters, apiClient)
                }
            },
            onClearAllFilters = {
                selectedFilters = emptyMap()
                coroutineScope.launch {
                    entities = apiClient.getAllSpells()
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Display filtered content based on selected filters
        ListView(entities = entities)
    }
}

suspend fun fetchFilteredEntities(
    selectedFilters: Map<String, List<String>>,
    apiClient: ApiClient
): List<IItem> {
    val allSpells = apiClient.getAllSpells()

    // Basic filter logic based on selectedFilters
    return allSpells.filter { spell ->
        selectedFilters.all { (category, options) ->
            when (category) {
                "Level" -> options.contains(spell.level.toString())
                else -> true
            }
        }
    }
}