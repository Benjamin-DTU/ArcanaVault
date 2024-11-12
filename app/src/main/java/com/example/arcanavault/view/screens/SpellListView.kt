package com.example.arcanavault.view.screens

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.arcanavault.controller.api.ApiClient
import com.example.arcanavault.model.data.IEntity
import com.example.arcanavault.view.components.ListView
import kotlinx.coroutines.launch

@Composable
fun SpellListView(
    apiClient: ApiClient,
    modifier: Modifier = Modifier
) {
    val entities = remember { mutableStateListOf<IEntity>() }
    val coroutineScope = rememberCoroutineScope()

    // Fetch data when SpellListView is loaded
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val fetchedEntities = apiClient.getAllSpells()
            entities.addAll(fetchedEntities)
        }
    }

    // Pass the list of entities to ListView for display
    ListView(
        entities = entities,
        modifier = modifier
    )
}
