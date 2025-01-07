package com.example.arcanavault.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.arcanavault.AppState
import com.example.arcanavault.controller.api.ApiClient
import com.example.arcanavault.model.data.IItem
import com.example.arcanavault.model.data.Spell
import com.example.arcanavault.ui.components.FilterView
import com.example.arcanavault.ui.components.ListView
import kotlinx.coroutines.launch

@Composable
fun SpellListView(
    apiClient: ApiClient,
    appState: AppState,
    onSpellSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var filters by remember { mutableStateOf(emptyMap<String, List<String>>()) }
    var selectedFilters by remember { mutableStateOf(emptyMap<String, List<String>>()) }
    var items by remember { mutableStateOf<List<IItem>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()

    // Fetch spells from API and generate filter options
    LaunchedEffect(Unit) {
        val spells = apiClient.getAllSpells()
        filters = Spell.generateFilterOptions(spells)
        items = spells
        appState.listOfSpells = spells
    }

    Column(modifier = modifier.fillMaxSize()) {
        FilterView(
            filterOptions = filters,
            selectedFilters = selectedFilters,
            onFilterChange = { category, options ->
                selectedFilters = selectedFilters.toMutableMap().apply { this[category] = options }
                coroutineScope.launch {
                    items = fetchFilteredEntities(selectedFilters, apiClient)
                }
            },
            onClearAllFilters = {
                selectedFilters = emptyMap()
                coroutineScope.launch {
                    items = apiClient.getAllSpells()
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Display filtered content based on selected filters
        ListView(
            items = items,
            appState = appState,
            onItemClick = { selectedSpell ->
                onSpellSelected(selectedSpell)
            }
        )
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
                "School" -> options.contains(spell.school?.name.toString())
                "Classes" -> spell.classes.any { it.name in options }
                "Casting Time" -> options.contains(spell.castingTime)
                "Damage Type" -> options.contains(spell.damage?.damageType?.name.toString())
                "Components" -> options.any { it in spell.components }
                "Concentration" -> options.contains(spell.concentration.toString())
                "Ritual" -> options.contains(spell.ritual.toString())
                else -> true
            }
        }
    }
}