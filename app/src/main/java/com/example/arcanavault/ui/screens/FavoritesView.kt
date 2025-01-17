package com.example.arcanavault.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.arcanavault.AppState
import com.example.arcanavault.DB.FunctionsDB
import com.example.arcanavault.model.data.Spell
import com.example.arcanavault.ui.components.ListView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun FavouritesView(
    appState: AppState,
    onBackClick: () -> Unit,
    onSpellSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val functionsDB = remember { FunctionsDB() }
    var favoriteSpells by remember { mutableStateOf(listOf<Spell>()) }


    LaunchedEffect(Unit) {
        favoriteSpells = withContext(Dispatchers.IO) {
            functionsDB.getFavoriteSpells()
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBackIosNew,
                contentDescription = "Back",
                modifier = Modifier
                    .padding(8.dp)
                    .height(24.dp)
                    .padding(end = 8.dp)
                    .clickable { onBackClick() }
            )
            Text(
                text = "Back",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { onBackClick() }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

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
