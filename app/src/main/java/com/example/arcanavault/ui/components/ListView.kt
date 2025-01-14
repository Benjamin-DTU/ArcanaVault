package com.example.arcanavault.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.example.arcanavault.AppState
import com.example.arcanavault.model.data.IItem

@Composable
fun ListView(
    items: List<IItem>,
    onItemClick: (String) -> Unit,
    appState: AppState,
    modifier: Modifier = Modifier
) {

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(2.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(items) { item ->
            ListItemView(
                imageUrl = item.imageUrl,
                school = item.school,
                level = item.level,
                name = item.name,
                isFavorite = item.isFavorite,
                onFavoriteClick = { appState.setSpellToFavorite(item) },
                modifier = Modifier.fillMaxWidth().clickable { onItemClick(item.index) }
            )
        }
    }
}
