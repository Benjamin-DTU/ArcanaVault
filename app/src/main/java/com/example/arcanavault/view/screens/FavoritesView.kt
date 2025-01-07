package com.example.arcanavault.view.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.arcanavault.AppState
import com.example.arcanavault.controller.api.ApiClient
import com.example.arcanavault.model.data.IItem
import com.example.arcanavault.view.components.ListView

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
            appState = appState,
            onItemClick = { selectedSpell ->
                onSpellSelected(selectedSpell)
            }
        )
    }
}

