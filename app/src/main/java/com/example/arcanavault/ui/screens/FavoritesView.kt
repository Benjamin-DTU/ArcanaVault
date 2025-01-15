package com.example.arcanavault.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.arcanavault.AppState
import com.example.arcanavault.ui.components.ListView

@Composable
fun FavouritesView(
    appState: AppState,
    onBackClick: () -> Unit,
    onSpellSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
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

        ListView(
            items = appState.listOfSpells.filter { it.isFavorite },
            onItemClick = { selectedSpell -> onSpellSelected(selectedSpell) },
            onFavoriteClick = { spell -> appState.setSpellToFavorite(spell) },
            titleProvider = { spell -> spell.name },
            detailsProvider = { spell ->
                listOf(
                    "Level: ${spell.level}",
                    "School: ${spell.index}"
                )
            }
        )

    }
    Column {
    if (appState.listOfSpells.filter { it.isFavorite }.isEmpty()) {
        Spacer (modifier = Modifier.height(48.dp))
        Text(
            text = "No favourites have been saved. " +
                    "Click on the star icon to save a spell to your favourites.",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(8.dp)
        )
    }
    }
}

