package com.example.arcanavault.view.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.arcanavault.model.data.IItem

@Composable
fun ListView(
    items: List<IItem>,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items) { item ->
            ListItemView(
                imageUrl = item.url,
                name = item.name,
                description = item.shortDescription,
                isFavorite = item.isFavorite,
                onFavoriteClick = { /* TODO: Implement favorite click action */ },
                modifier = Modifier.fillMaxWidth().clickable { onItemClick(item.index) }
            )
        }
    }
}