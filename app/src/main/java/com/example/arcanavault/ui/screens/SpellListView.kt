package com.example.arcanavault.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import kotlinx.coroutines.launch
import com.example.arcanavault.AppState
import com.example.arcanavault.controller.api.ApiClient
import com.example.arcanavault.model.data.IItem
import com.example.arcanavault.model.data.Spell
import com.example.arcanavault.ui.components.ListView
import com.example.arcanavault.ui.components.SpellsAppBar

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
            SpellsAppBar(
                title = "Spells",
                onFilterClick = { showFilterScreen = true },
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Conditionally render either the FilterScreen OR the spell list
            if (showFilterScreen) {
                // Filter screen is visible, hide the list
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
                        // Reset all filters
                        selectedFilters = emptyMap()
                        coroutineScope.launch {
                            items = apiClient.getAllSpells()









                        }
                    },
                    onNavigateBack = {
                        // Hide filter screen, reveal list
                        showFilterScreen = false
                    }
                )
            } else {
                // Filter screen is hidden; show the (filtered) spell list
                //Spacer(modifier = Modifier.height(16.dp))

                ListView(
                    items = items,
                    appState = appState,
                    onItemClick = { selectedSpell ->
                        onSpellSelected(selectedSpell)
                    }
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
