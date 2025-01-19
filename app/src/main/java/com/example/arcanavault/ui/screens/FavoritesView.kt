package com.example.arcanavault.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouritesView(
    appState: AppState,
    onSpellSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val functionsDB = remember { FunctionsDB() }
    var favoriteSpells by remember { mutableStateOf(listOf<Spell>()) }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

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
                buttons = listOf(),
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
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
                        items = favoriteSpells,
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
